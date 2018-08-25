/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exercise4;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class App {


    public static void main(String[] args) throws Exception {
        Parser.parse();
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);

    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class HelloWorldProducer implements Runnable {

        public void run() {
            try {
                ActiveMQConnectionFactory activeConnectionFactory = new ActiveMQConnectionFactory("vm://localhost");
                Connection connection = activeConnectionFactory.createConnection();
                connection.start();
                Session newSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = newSession.createTopic("Example.Library.Publication");
                MessageProducer producer = newSession.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                for(int i=0;i<Parser.authorsList.size();++i){
                    String text = Parser.authorsList.get(i);
                    TextMessage message = newSession.createTextMessage(text);
                    System.out.println("Sent message: "+text);
                    producer.send(message);
                }
                for(int i=0;i<Parser.booksList.size();++i){
                    String text = Parser.booksList.get(i);
                    TextMessage message = newSession.createTextMessage(text);
                    System.out.println("Sent message: "+text);
                    producer.send(message);
                }
                
                newSession.close();
                connection.close();

            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }
    }

    public static class HelloWorldConsumer implements Runnable, ExceptionListener {

        public void run() {
            try {
                ActiveMQConnectionFactory activeConnectionFactory = new ActiveMQConnectionFactory("vm://localhost");
                Connection connection = activeConnectionFactory.createConnection();
                connection.start();
                connection.setExceptionListener(this);
                Session newSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = newSession.createTopic("Example.Library.Publication");
                MessageConsumer consumer = newSession.createConsumer(destination);

                for(int i=0;i<Parser.authorsList.size();++i){
                    Message message = consumer.receive(1000);

                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        String text = textMessage.getText();
                        System.out.println("Received: " + text);
                    } else {
                        System.out.println("Received: " + message);
                    }
                }
                for(int i=0;i<Parser.booksList.size();++i){
                    Message message = consumer.receive(1000);

                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        String text = textMessage.getText();
                        System.out.println("Received: " + text);
                    } else {
                        System.out.println("Received: " + message);
                    }
                }
                consumer.close();
                newSession.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }

        public synchronized void onException(JMSException ex) {
            System.out.println("JMS Exception occured.  Shutting down client.");
        }
    }
}
