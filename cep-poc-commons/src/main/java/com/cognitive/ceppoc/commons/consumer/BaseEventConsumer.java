package com.cognitive.ceppoc.commons.consumer;

import com.cognitive.ceppoc.commons.props.CommonProps;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Properties;

@Component
public class BaseEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEventConsumer.class);

    private CommonProps commonProps;

    public BaseEventConsumer(CommonProps commonProps) {
        this.commonProps = commonProps;
    }

    public Consumer<Long, String> createConsumer() {
        return createConsumer(commonProps.getTopic());
    }

    public Consumer<Long, String> createConsumer(String topic) {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, commonProps.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, commonProps.getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Create the consumer using props.
        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);

        // Subscribe to the topic.
        LOGGER.info("Subscribing to topic '{}'", topic);
        consumer.subscribe(Collections.singletonList(topic));
        return consumer;
    }
}
