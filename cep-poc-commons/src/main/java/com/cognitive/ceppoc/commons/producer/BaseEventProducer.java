package com.cognitive.ceppoc.commons.producer;

import com.cognitive.ceppoc.commons.props.CommonProps;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class BaseEventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEventProducer.class);

    private CommonProps commonProps;

    @Autowired
    public BaseEventProducer(CommonProps commonProps) {
        this.commonProps = commonProps;
    }

    public Producer<Long, String> createProducer() {
        LOGGER.info("Creating producer for server {} and clientId {}", commonProps.getBootstrapServers(), commonProps.getClientId());
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, commonProps.getBootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, commonProps.getClientId());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }
}
