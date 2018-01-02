package com.cognitive.ceppoc.transactionconsumer;

import com.cognitive.ceppoc.commons.consumer.BaseEventConsumer;
import com.cognitive.ceppoc.commons.event.transaction.Transaction;
import com.cognitive.ceppoc.commons.props.CommonProps;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TransactionConsumer implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionConsumer.class);

    private CommonProps commonProps;

    private BaseEventConsumer baseEventConsumer;

    private TransactionHandler transactionHandler;

    @Autowired
    public TransactionConsumer(CommonProps commonProps, BaseEventConsumer baseEventConsumer, TransactionHandler transactionHandler) {
        this.commonProps = commonProps;
        this.baseEventConsumer = baseEventConsumer;
        this.transactionHandler = transactionHandler;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        listenTransactionEvents();
    }

    private void listenTransactionEvents() {
        Consumer<Long, String> consumer = baseEventConsumer.createConsumer();
        int giveUp = 1000;
        int noRecordsCount = 0;

        ObjectMapper mapper = new ObjectMapper();
        LOGGER.info("Listening for transaction events");
        while (true) {
            final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);

            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }

            consumerRecords.forEach(record -> {
                LOGGER.debug("Consumer Record:({}, {}, {}, {})", record.key(), record.value(), record.partition(), record.offset());
                Transaction transaction = null;
                try {
                    transaction = mapper.readValue(record.value(), Transaction.class);
                    transactionHandler.handleTransaction(transaction);
                } catch (IOException e) {
                    LOGGER.error("Error deserializing message", e);
                }
            });
            consumer.commitAsync();
        }
        consumer.close();
        LOGGER.info("DONE");
    }
}
