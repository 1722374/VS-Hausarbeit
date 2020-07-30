package Aufgabe_6;


import org.eclipse.paho.client.mqttv3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;

class MQTTPerformance {

	private static Instant empfangen = null; // muss globale Variable sein, damit sie über innere Klasse gesetzt werden kann
	public static void main(String[] args) throws MqttException {
		MqttClient client = null;

		String clientId = MqttClient.generateClientId();

		//Kommentieren Sie den zu testenden Broker aus.

		String broker = Conf.ECLIPSEBROKER;   //("tcp://mqtt.eclipse.org:1883")

		//String broker = Conf.HIVEEBROKER; // "tcp://broker.hivemq.com:1883";
		String topic = Conf.TOPIC; // Topic =  "Performance";



		try {
			client = new MqttClient(broker, clientId);
			String message = "Test Nachricht um Performance zu ermitteln"; //Nachricht mit der getestet wird
			System.out.println("Nachrichtenformat: " + message.getClass().getName() + " Größe in bytes: " + message.getBytes().length + " Inhalt: " + message);
			int qos = 2;
			long[] durations = new long[10]; // Dauer der einzelnen schleifendurchläufe werden in diesem Array gespeichert

			// setCallback mit innerer Klasse
			client.setCallback(new MqttCallback() {

				@Override
				public void connectionLost(Throwable arg0) {
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken arg0) {

						System.out.println("Nachricht empfangen");
				}

				@Override
				public void messageArrived(String topic, MqttMessage m) throws Exception {
					 // Zeitraum wenn Nachricht empfangen wurde
					empfangen = Instant.now();
					System.out.println(empfangen);
					System.out.println("Topic: " + topic + ",  Message: " + m.toString());
				}
			});

			client.connect();
			client.subscribe(topic); //Um Nachrichten empfangen zu können

			for (int i=0; i<10; i++) {

				Instant begin = Instant.now();
				sendMessage(client, topic,  message, qos);
				Thread.sleep(800); // warten, damit die Nachricht empfangen werden kann. Dies beeinflusst nicht die gemessene Zeit, da die Methode asynchron aufgerufen wird.
				System.out.println(empfangen);
				Duration duration = Duration.between(begin,empfangen);
				durations[i] = duration.toMillis();
				empfangen = null; //Abschließend empfangen wieder auf Null setzen
			}
			double a = average(durations); // durchschnitt
			double s = deviation(durations, a); // Standardabweichung
			System.out.println("Die durchschnittliche Zeitdauer beträgt: " + a + " Millisekunden");
			System.out.println("Die Standardabweichung beträgt: " + s + " Millisekunden");


		} catch (MqttException | InterruptedException e) {
			System.err.println(e.getMessage());
		} finally {
			if ( client != null) {
				client.disconnect();
				client.close();
			}
		}
	}

	private static void sendMessage(MqttClient client, String topic, String nachricht, int qos ) throws MqttException {
		MqttMessage message = new MqttMessage();
		message.setPayload(nachricht.getBytes());
		//System.out.println("Message wurde gesendet");
		client.publish(topic, message.getPayload(), qos, false);
	}

	private static double average (long[] times) {
		double total = 0;
		for(double time : times){
			total +=  time;
		}

		double average = total / times.length;

		return average;
	}

	private static double deviation(long times[], double average)
	{
		double varianceNumerator =0.0; //Varianzzähler

		for(long time :  times) {
			varianceNumerator += Math.pow(time - average, 2);
		}

		return Math.sqrt(varianceNumerator/times.length);
	}








}
