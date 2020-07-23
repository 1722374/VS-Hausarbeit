package Aufgabe_5;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import Aufgabe_5.Conf;

public class JMSConsumer implements MessageListener {
	private Connection connection;
	private Session session;
	private MessageConsumer consumer;


	public JMSConsumer(String receiveDest) throws NamingException, JMSException {
		Context ctx = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
		connection = factory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destIn = (Destination) ctx.lookup(receiveDest);
		consumer = session.createConsumer(destIn);
		consumer.setMessageListener(this);
		connection.start();

	}

	@Override
	public void onMessage(Message message) {

		if (message instanceof BytesMessage) {
			BytesMessage bytesMessage = (BytesMessage) message;
			String messageString;


			try {
				byte[] b = new byte[(int) bytesMessage.getBodyLength()];
				bytesMessage.readBytes(b);
				messageString = new String(b);
				System.out.println(messageString);
			} catch (JMSException e) {
				System.err.println(e);
			}
		}
	}


	public static void main(String[] args) {
		JMSConsumer node = null;
	 	int sleep_ms = 200000;

		try {
			node = new JMSConsumer(Conf.TOPIC); // TOPIC = "var.mom.jms.aufgabenummer5";   jindi= opic.var.mom.jms.aufgabenummer5 = aufgabenummer5
			Thread.sleep(sleep_ms);

		} catch (NamingException | JMSException | InterruptedException e) {
			System.err.println(e);
		} finally {
			try {
				if (node != null && node.consumer != null) {
					node.consumer.close();
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

