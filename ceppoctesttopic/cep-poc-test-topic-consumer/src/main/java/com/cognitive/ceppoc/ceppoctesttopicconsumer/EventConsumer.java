package com.cognitive.ceppoc.ceppoctesttopicconsumer;

import com.cognitive.ceppoc.commons.consumer.BaseEventConsumer;
import com.cognitive.ceppoc.commons.props.CommonProps;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);

    private CommonProps commonProps;

    private BaseEventConsumer baseEventConsumer;

    @Autowired
    public EventConsumer(CommonProps commonProps, BaseEventConsumer baseEventConsumer) {
        this.commonProps = commonProps;
        this.baseEventConsumer = baseEventConsumer;
    }

    public void runConsumer() {
        final Consumer<Long, String> consumer = baseEventConsumer.createConsumer();

        final int giveUp = 100;
        int noRecordsCount = 0;

        while (true) {
            final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);

            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }

            consumerRecords.forEach(record -> {
                LOGGER.info("Consumer Record:({}, {}, {}, {})", record.key(), record.value(), record.partition(), record.offset());
            });
            consumer.commitAsync();
        }
        consumer.close();
        LOGGER.info("DONE");
    }

}
