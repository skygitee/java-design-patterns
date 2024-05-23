package com.iluwatar.publishsubscribe;

import lombok.extern.slf4j.Slf4j;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class Borrower implements MessageListener {

  private TopicConnection tConnection;
  private TopicSession tSession;
  private Topic topic;
  private final double currentRate;
  private static final String ERROR = "An error has occured!";
  private double newRate;

  public Borrower(String topicCFName, String topicName, double initialRate) {

    currentRate = initialRate;

    try {
      //create context and retrieve objects from directory
      Context context = new InitialContext();
      TopicConnectionFactory topicCF = (TopicConnectionFactory) context.lookup(topicCFName);
      tConnection = topicCF.createTopicConnection();

      //create connection
      tSession = tConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

      //Retrieve request and response queues
      topic = (Topic) context.lookup(topicName);

      //Create subscriber and message listener
      TopicSubscriber subscriber = tSession.createSubscriber(topic);
      //Adds event listener to subscriber and uses onMessage as a callback
      subscriber.setMessageListener(this);

      tConnection.start();
      LOGGER.info("Initial rate is " + currentRate + " \nWaiting for new rates...");
    } catch(NamingException e) {
      LOGGER.error(ERROR, e);
    } catch(JMSException e) {
      LOGGER.error(ERROR, e);
    }
  }

  //This method is called asynchronously by the activeMQ broker
  public void onMessage(Message message) {
    try {
      BytesMessage bMessage = (BytesMessage) message;
      double newRate = bMessage.readDouble();
      setNewRate(newRate);

      if (currentRate - newRate >= 1)
        System.out.println("New Rate is " + newRate + " - Consider refinancing");
      else
        System.out.println("New Rate is " + newRate + " - Consider keeping current rate");
    } catch(JMSException e) {
      LOGGER.error(ERROR, e);
    }
    LOGGER.info("Waiting for new rates...");
  }

  public boolean close() {
    try {
      tConnection.close();
      return true;
    } catch(JMSException e) {
      LOGGER.error(ERROR, e);
      return false;
    }
  }

  public static void main(String[] args) {

    String topicCF = null;
    String topicName = null;
    int rate = 0;
    if (args.length == 3) {
      topicCF = args[0];
      topicName = args[1];
      rate = Integer.parseInt(args[2]);
    } else {
      LOGGER.info("Invalid arguments. Should be: ");
      LOGGER.info("java TBorrower [factory] [topic] [rate]");
      System.exit(1);
    }

    Borrower borrower = new Borrower(topicCF, topicName, rate);

    try {
      // Run until enter is pressed
      BufferedReader reader = new BufferedReader
          (new InputStreamReader(System.in));
      LOGGER.info("TBorrower application started");
      LOGGER.info("Press enter to quit application");
      reader.readLine();
      borrower.close();
      System.exit(0);
    } catch (IOException e) {
      LOGGER.error(ERROR, e);
    }
  }

  public TopicConnection gettConnection() {
    return tConnection;
  }

  public TopicSession gettSession() {
    return tSession;
  }

  public Topic getTopic() {
    return topic;
  }

  public double getCurrentRate() {
    return currentRate;
  }

  public double getNewRate() { return newRate; }

  private void setNewRate(double newRate) { this.newRate = newRate; }
}
