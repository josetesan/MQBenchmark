package com.josetesan.brokers.benchmark.test;


import com.josetesan.brokers.benchmark.JMSConsumer;
import com.josetesan.brokers.benchmark.JMSProducer;
import com.josetesan.brokers.benchmark.apollo.ApolloStompConsumer;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.josetesan.brokers.benchmark.apollo.ApolloStompProducer;
import com.rabbitmq.client.Delivery;

@PerfTest(duration=30000,threads=16)
public class TestApolloSpeed {
    
    @Rule
    public ContiPerfRule i = new ContiPerfRule();
    
    
    private static JMSConsumer consumer;
    private static JMSProducer producer;
    private static Delivery delivery = null;

    @BeforeClass
    public static void setUp()  {
        consumer = new ApolloStompConsumer();
        producer = new ApolloStompProducer();
    }

    @Test
    public void test() {
        try {
            producer.run();
            delivery = (Delivery)consumer.run();
            Assert.assertNotNull(delivery);
        } catch (Exception e) {
            
        }
    }

    
    
    

    
}
