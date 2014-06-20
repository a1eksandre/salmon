import sqlite3

conn = sqlite3.connect('my.db')
c = conn.cursor()

c.execute('''DROP TABLE IF EXISTS feeds''')
c.execute('''CREATE TABLE feeds(id INTEGER PRIMARY KEY,  message TEXT, stamp timestamp)''')

conn.commit()
conn.close()
