package com.josetesan.brokers.benchmark.test;


import com.josetesan.brokers.benchmark.JMSConsumer;
import com.josetesan.brokers.benchmark.JMSProducer;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.josetesan.brokers.benchmark.rabbitmq.RabbitMQConsumer;
import com.josetesan.brokers.benchmark.rabbitmq.RabbitMQProducer;

import static org.junit.Assert.fail;

@PerfTest(duration=30000,threads=16)
public class TestRabbitSpeed {
    
    @Rule
    public ContiPerfRule i = new ContiPerfRule();
    
    
    private static JMSConsumer consumer;
    private static JMSProducer producer;

    @BeforeClass
    public static void setUp()  {
        consumer = new RabbitMQConsumer();
        producer = new RabbitMQProducer();
    }

    @Test
    public void test() {
        try {
            producer.run();
            String tag = (String)consumer.run();
            Assert.assertNotNull(tag);
        } catch (Exception e) {
            fail("Nothing received "+e);
        }
    }

    
    
    @AfterClass
    public static void tearDown() throws Exception {
        consumer.stop();
        producer.stop();
    }

    
}
