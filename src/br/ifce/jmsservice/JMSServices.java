package br.ifce.jmsservice;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Iterator;

/**
 * Created by yurir on 28/02/2017.
 */

@WebService
public interface JMSServices {
    @WebMethod public void createQueue(String queueName);
    @WebMethod public void createTopic(String topicName);
    @WebMethod public void deleteDestination(String destName);
    @WebMethod public Integer messagesInQueue(String queueName);
    @WebMethod public void createUser(String userName);
    @WebMethod public Iterator listDestinations();
}
