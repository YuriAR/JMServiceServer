package br.ifce.jmsservice;

import br.ifce.User;
import org.exolab.jms.administration.AdminConnectionFactory;
import org.exolab.jms.administration.JmsAdminServerIfc;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.jws.WebService;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by yurir on 28/02/2017.
 */

@WebService(endpointInterface = "br.ifce.jmsservice.JMSServices")
public class JMSManager implements JMSServices {

    String url = "tcp://localhost:9999/";
    List<User> users;
    JmsAdminServerIfc admin;

    public JMSManager(){
        this.users = new ArrayList<>();
    }

    @Override
    public void createQueue(String queueName) {
        try {
            Boolean isQueue = Boolean.TRUE;
            if (!getAdmin().addDestination(queueName, isQueue)) {
                System.err.println("Failed to create queue " + queueName);
            }
        }
        catch (Exception e){
            System.err.println("Failed to create queue " + queueName);
        }
    }

    @Override
    public void createTopic(String topicName) {
        try {
            Boolean isQueue = Boolean.FALSE;
            if (!getAdmin().addDestination(topicName, isQueue)) {
                System.err.println("Failed to create topic " + topicName);
            }
        }
        catch (Exception e){
            System.err.println("Failed to create queue " + topicName);
        }
    }

    @Override
    public void createUser(String userName) {
        User user = new User(userName);
        if (!user.userExists(users)){
            createQueue(user.queueName);
            users.add(user);
        }
        else{
            System.err.println("Failed to create user " + userName);
        }
    }

    @Override
    public void deleteDestination(String destName) {
        try{
            if (!getAdmin().removeDestination(destName)) {
                System.err.println("Failed to remove destination " + destName);
            }
        }
        catch (Exception e){
            System.err.println("Failed to remove destination " + destName);
        }
    }

    @Override
    public Integer messagesInQueue(String queueName) {
        try{
            return getAdmin().getQueueMessageCount(queueName);
        }
        catch (Exception e){
            System.err.println("Failed to count messages in " + queueName);
            return 0;
        }

    }

    @Override
    public List<String> listQueues(){
        List<String> toReturn = new ArrayList<>();
        try {
            Vector destinations = getAdmin().getAllDestinations();
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
            Vector destinations = getAdmin().getAllDestinations();
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
            return new ArrayList<>();
        }
    }

    private JmsAdminServerIfc getAdmin() throws MalformedURLException, JMSException{
        if (admin == null){
            admin = AdminConnectionFactory.create(url);
        }
        return admin;

    }
}
