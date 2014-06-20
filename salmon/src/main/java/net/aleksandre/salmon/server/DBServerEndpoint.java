package net.aleksandre.salmon.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import net.aleksandre.salmon.Controller.TextController;
import net.aleksandre.salmon.model.Text;

@ServerEndpoint(value = "/dbserver")
public class DBServerEndpoint {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private TextController textController = new TextController();

	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected session with id " + session.getId());
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		logger.info("Session id: " + session.getId() + ", received message: " + message);
		if ("get".equals(message)) {
			echoAll(session, getTexts());
		} else if (message != null && message.startsWith("add ")) {
			if (message.substring(4).trim().length() > 0) {
				addText(message.substring(4).trim());
				echoAll(session, getTexts());
			} else {
				try {
					session.getBasicRemote().sendText("Invalid empty message");
				} catch (IOException e) {
					logger.severe(e.getMessage());
				}
			}
		} else {
			try {
				session.getBasicRemote().sendText("Invalid request: " + message);
			} catch (IOException e) {
				logger.severe(e.getMessage());
			}
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
	}

	private String getTexts() {
		StringBuilder sb = new StringBuilder();
		for (Text t : textController.getTexts()) {
			sb.append(t).append("<br>");
		}
		return sb.toString();
	}

	private void addText(String txt) {
		Text t = new Text(txt);
		textController.saveText(t);
	}

	private void echoAll(Session session, String msg) {
		try {
			for (Session sess : session.getOpenSessions()) {
				if (sess.isOpen())
					sess.getBasicRemote().sendText(msg);
			}
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

}
