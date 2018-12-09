package com.josetesan.brokers.benchmark.test;


import com.josetesan.brokers.benchmark.JMSConsumer;
import com.josetesan.brokers.benchmark.JMSProducer;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.josetesan.brokers.benchmark.activemq.ActiveMQConsumer;
import com.josetesan.brokers.benchmark.activemq.ActiveMQProducer;
import com.rabbitmq.client.Delivery;

@PerfTest(duration=30000,threads=16)
public class TestActiveMQSpeed {
    
    @Rule
    public ContiPerfRule i = new ContiPerfRule();
    
    
    private static JMSConsumer consumer;
    private static JMSProducer producer;
    private static Delivery delivery = null;

    @BeforeClass
    public static void setUp()  {
        consumer = new ActiveMQConsumer();
        producer = new ActiveMQProducer();
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
