import tornado.websocket
import tornado.ioloop
import tornado.web
import sqlite3
from datetime import datetime
from tornado.options import define, options

listeners = []
db_path = 'my.db'


class IndexHandler(tornado.web.RequestHandler):
    @tornado.web.asynchronous
    def get(self):
        self.render('index.html')


class WebSocketHandler(tornado.websocket.WebSocketHandler):
    def open(self, *args):
        print 'whohoo'
        self.stream.set_nodelay(True)
        listeners.append(self)
        self.conn = sqlite3.connect(db_path)
        cur = self.conn.cursor()
        # read db and send messages one by one
        for row in cur.execute('''SELECT message from feeds'''):
            self.write_message(row[0])


    def on_message(self, message):
        try:
            for listener in listeners:
                listener.write_message(message)
            cur = self.conn.cursor()
            cur.execute('''INSERT INTO feeds (message, stamp) VALUES(?,?)''',
                        (message, datetime.now()))
            self.conn.commit()
        except:
            raise
        print "received a message : %s" % (message)

    def on_close(self):
        listeners.remove(self)
        self.conn.close()

define("port", default=8888, help="run on the given port", type=int)   

app = tornado.web.Application([
    (r'/', IndexHandler),
    (r'/ws', WebSocketHandler),
])

if __name__ == '__main__':
    tornado.options.parse_command_line()
    app.listen(options.port)
    tornado.ioloop.IOLoop.instance().start()
