package br.ifce;

import br.ifce.jmsservice.JMSManager;

import javax.xml.ws.Endpoint;

public class Main {

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:9999/jmsservice", new JMSManager());
    }
}
