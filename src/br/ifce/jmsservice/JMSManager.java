package br.ifce.jmsservice;

import org.exolab.jms.administration.AdminConnectionFactory;
import org.exolab.jms.administration.JmsAdminServerIfc;

import javax.jms.*;
import javax.jms.Queue;
import javax.jws.WebService;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.*;

/**
 * Created by yurir on 28/02/2017.
 */

@WebService(endpointInterface = "br.ifce.jmsservice.JMSServices")
public class JMSManager implements JMSServices {

    String url = "tcp://localhost:3035/";
    JmsAdminServerIfc admin;
    Context context;
    ConnectionFactory factory;
    Connection connection;
    Session session;

    public JMSManager(){
        try {
            this.admin = AdminConnectionFactory.create(url);
            Hashtable properties = new Hashtable();
            properties.put(Context.INITIAL_CONTEXT_FACTORY,
                    "org.exolab.jms.jndi.InitialContextFactory");
            properties.put(Context.PROVIDER_URL, url);

            this.context = new InitialContext(properties);
            this.factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            this.connection = factory.createConnection();
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void createQueue(String queueName) {
        try {
            Boolean isQueue = Boolean.TRUE;
            if (!admin.addDestination(queueName, isQueue)) {
                System.err.println("Failed to create queue " + queueName);
            }
        }
        catch (Exception e){
            System.err.println("Failed to create queue " + queueName);
            e.printStackTrace();
        }
    }

    @Override
    public void createTopic(String topicName) {
        try {
            Boolean isQueue = Boolean.FALSE;
            if (!admin.addDestination(topicName, isQueue)) {
                System.err.println("Failed to create topic " + topicName);
            }
        }
        catch (Exception e){
            System.err.println("Failed to create topic " + topicName);
            e.printStackTrace();
        }
    }

    @Override
    public boolean createUser(String userName) {
        if (!queueExists(userName)){
            createQueue(userName);
            return true;
        }
        else{
            System.err.println("Failed to create user " + userName);
            return false;
        }
    }

    @Override
    public boolean queueExists(String queueName) {
        try {
            if (admin.destinationExists(queueName)) {
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public void deleteDestination(String destName) {
        try{
            if (!admin.removeDestination(destName.split("-")[0].replace(" ", ""))) {
                System.err.println("Failed to remove destination " + destName);
            }
        }
        catch (Exception e){
            System.err.println("Failed to remove destination " + destName);
            e.printStackTrace();
        }
    }

    @Override
    public Integer messagesInQueue(String queueName) {
        try{
            return admin.getQueueMessageCount(queueName);
        }
        catch (Exception e){
            System.err.println("Failed to count messages in " + queueName);
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public List<String> listQueues(){
        List<String> toReturn = new ArrayList<>();
        try {
            Vector destinations = admin.getAllDestinations();
            Iterator iterator = destinations.iterator();
            while (iterator.hasNext()) {
                Destination destination = (Destination) iterator.next();
                if (destination instanceof Queue) {
                    Queue queue = (Queue) destination;
                    toReturn.add(queue.getQueueName());
                }
            }
            return toReturn;
        }
        catch (Exception e){
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> listTopics(){
        List<String> toReturn = new ArrayList<>();
        try {
            Vector destinations = admin.getAllDestinations();
            Iterator iterator = destinations.iterator();
            while (iterator.hasNext()) {
                Destination destination = (Destination) iterator.next();
                if (destination instanceof Topic) {
                    Topic topic = (Topic) destination;
                    toReturn.add(topic.getTopicName());
                }
            }
            return toReturn;
        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void putMessageInQueue(String queueName, String msg) {
        try {
            Destination destination = (Destination) context.lookup(queueName);
            connection.start();
            MessageProducer sender = session.createProducer(destination);
            TextMessage message = session.createTextMessage(msg);
            sender.send(message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getMessageFromQueue(String queueName) {
        try {
            Destination destination = (Destination) context.lookup(queueName);
            MessageConsumer receiver = session.createConsumer(destination);
            connection.start();

            TextMessage message = (TextMessage) receiver.receive();
            System.out.println("Received message: " + message.getText());
            return message.getText();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void subscribeToTopic(String topicName) {
        System.out.println("Não implementado");
    }

    @Override
    public void sendMessageToTopic(String topicName) {
        System.out.println("Não implementado");
    }
}
