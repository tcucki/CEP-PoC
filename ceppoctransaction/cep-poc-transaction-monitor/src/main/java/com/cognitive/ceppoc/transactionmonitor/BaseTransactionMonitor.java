package com.cognitive.ceppoc.transactionmonitor;

import com.cognitive.ceppoc.commons.consumer.BaseEventConsumer;
import com.cognitive.ceppoc.commons.props.CommonProps;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public abstract class BaseTransactionMonitor implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTransactionMonitor.class);

    private CommonProps commonProps;

    private BaseEventConsumer baseEventConsumer;

    private String topic;

    public BaseTransactionMonitor(CommonProps commonProps, BaseEventConsumer baseEventConsumer, String topic) {
        this.commonProps = commonProps;
        this.baseEventConsumer = baseEventConsumer;
        this.topic = topic;
    }

    @Override
    public void run() {
        Consumer<String, String> consumer = createConsumer();
        int giveUp = 1000;
        int noRecordsCount = 0;

        ObjectMapper mapper = new ObjectMapper();
        LOGGER.info("Listening for transaction events");
        while (true) {
            final ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);

            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }

            consumerRecords.forEach(record -> {
                LOGGER.debug("Consumer Record:({}, {}, {}, {})", record.key(), record.value(), record.partition(), record.offset());
                if (record.value() != null) {
                    try {
                        Map<String, String> event = mapper.readValue(record.value(), Map.class);
                        processEvent(event);
                    } catch (IOException e) {
                        LOGGER.error("Error deserializing message", e);
                    }
                }
            });
            consumer.commitAsync();
        }
        consumer.close();
        LOGGER.info("DONE");
    }

    protected abstract void processEvent(Map<String, String> event);

    private Consumer<String, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, commonProps.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, commonProps.getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Create the consumer using props.
        final Consumer<String, String> consumer = new KafkaConsumer<>(props);

        // Subscribe to the topic.
        LOGGER.info("Subscribing to topic '{}'", topic);
        consumer.subscribe(Collections.singletonList(topic));
        return consumer;
    }
}
