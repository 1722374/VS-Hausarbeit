package Aufgabe_5;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.jms.TextMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import Aufgabe_5.Conf;

class MQTTPublisher {
	public static void main(String[] args) throws MqttException {
		MqttClient client = null;

		String clientId = MqttClient.generateClientId();
		String topic = Conf.TOPICMQTT ; // Topic = "aufgabenummer5";
		boolean retained = true;
		try {
			client = new MqttClient(Conf.BROKER, clientId); // Broker = "tcp://localhost:1883";
			//LWT Nachricht
			MqttConnectOptions options = new MqttConnectOptions();
			options.setWill(topic, "ich bin weg".getBytes(), 2,retained);
			client.connect(options);
			MqttMessage message = new MqttMessage();
			//Retained Nachricht
			String retainedMessage = "Hallo und herzlich willkommen! Ich bin der MQTT Publisher";
			client.publish(topic, retainedMessage.getBytes(), 2, retained);
			System.out.println("Retained gesendet");


			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			String line;
			System.out.println("Geben Sie eine Nachricht ein");

			while (true) {
				line = input.readLine();

				String m = line;
				message.setPayload(m.getBytes());
				client.publish(topic, message);

				// node.producer.send(node.session.createTextMessage(line));
			}


		} catch (MqttException | IOException e) {
			System.err.println(e.getMessage());
		} finally {


		}
	}
}
