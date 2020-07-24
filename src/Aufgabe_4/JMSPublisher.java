package Aufgabe_4;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class JMSPublisher {
	private Connection connection;
	private Session session;
	private MessageProducer producer;


	public JMSPublisher(String sendDest) throws NamingException, JMSException {
		Context ctx = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
		connection = factory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destOut = (Destination) ctx.lookup(sendDest);
		producer = session.createProducer(destOut);
		connection.start();
	}



	public static void main(String[] args) {
		JMSPublisher node = null;

		try {
			node = new JMSPublisher(Conf.TOPIC); // Topic = "var.mom.jms.aufgabe4" jindi = topic.var.mom.jms.aufgabe4 = aufgabe4
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			String line;
			System.out.println("Geben Sie eine Nachricht ein");
			String name = input.readLine();
			while (true) {
				line = input.readLine();
				TextMessage message = node.session.createTextMessage();
				message.setText(line);
				node.producer.send(message);

			}
		} catch (NamingException | JMSException | IOException e) {
			System.err.println(e);
		} finally {
			try {
				if (node != null && node.producer != null) {
					node.producer.close();
				}

				if (node != null && node.session != null) {
					node.session.close();
				}
				if (node != null && node.connection != null) {
					node.connection.close();
				}
			} catch (JMSException e) {
				System.err.println(e);
			}
		}
	}
}
