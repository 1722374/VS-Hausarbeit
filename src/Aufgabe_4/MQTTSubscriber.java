package Aufgabe_4;

import org.eclipse.paho.client.mqttv3.*;



class MQTTSubscriber {
	public static void main(String[] args) {
		MqttClient client = null;
		String topic = Conf.TOPICMQTT; // topic = "aufgabe4"
		String clientId = MqttClient.generateClientId();
		try {
			client = new MqttClient(Conf.BROKER, clientId); // Broker= "tcp://localhost:1883"
			client.setCallback(new MqttCallback() {

				@Override
				public void connectionLost(Throwable arg0) {
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken arg0) {
				}

				@Override
				public void messageArrived(String topic, MqttMessage m) throws Exception {
					System.out.println("Topic: " + topic + ",  Message: " + m.toString());
				}
			});
			client.connect();
			client.subscribe(topic);
			while (true) {
				Thread.sleep(1000);
			}
		} catch (MqttException | InterruptedException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				client.disconnect();
				client.close();
			} catch (MqttException e) {
				// unrecoverable
			}
		}
	}
}
