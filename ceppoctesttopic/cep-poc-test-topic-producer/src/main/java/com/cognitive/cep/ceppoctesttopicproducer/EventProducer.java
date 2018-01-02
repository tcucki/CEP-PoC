package com.cognitive.cep.ceppoctesttopicproducer;

import com.cognitive.ceppoc.commons.producer.BaseEventProducer;
import com.cognitive.ceppoc.commons.props.CommonProps;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class EventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProducer.class);

    private CommonProps commonProps;

    private BaseEventProducer baseEventProducer;

    @Autowired
    public EventProducer(CommonProps commonProps, BaseEventProducer baseEventProducer) {
        this.commonProps = commonProps;
        this.baseEventProducer = baseEventProducer;
    }

    public void runProducer(final int sendMessageCount) {
        final Producer<Long, String> producer = baseEventProducer.createProducer();
        long time = System.currentTimeMillis();

        try {
            for (long index = time; index < time + sendMessageCount; index++) {
                final ProducerRecord<Long, String> record =
                        new ProducerRecord<>(commonProps.getTopic(), index, "Sync message #" + index);

                RecordMetadata metadata = producer.send(record).get();

                long elapsedTime = System.currentTimeMillis() - time;

                LOGGER.info("sent record(key={} value={}) meta(partition={}, offset={}) time={}",
                        record.key(), record.value(), metadata.partition(),
                        metadata.offset(), elapsedTime);
            }
        } catch (Exception e) {
            LOGGER.error("Error sending sync message", e);
        } finally {
            producer.flush();
            producer.close();
        }
    }

    public void runProducerAsync(final int sendMessageCount) {
        final Producer<Long, String> producer = baseEventProducer.createProducer();
        long time = System.currentTimeMillis();
        final CountDownLatch countDownLatch = new CountDownLatch(sendMessageCount);

        try {
            for (long index = time; index < time + sendMessageCount; index++) {
                final ProducerRecord<Long, String> record =
                        new ProducerRecord<>(commonProps.getTopic(), index, "ASYNC event #" + index);

                producer.send(record, (metadata, exception) -> {
                    long elapsedTime = System.currentTimeMillis() - time;
                    if (metadata != null) {
                        LOGGER.info("sent async record(key={} value={}) meta(partition={}, offset={}) time={}",
                                record.key(), record.value(), metadata.partition(), metadata.offset(), elapsedTime);
                    } else {
                        exception.printStackTrace();
                    }
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await(25, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("Error sending event asynchronously", e);
        } finally {
            producer.flush();
            producer.close();
        }
    }
}
