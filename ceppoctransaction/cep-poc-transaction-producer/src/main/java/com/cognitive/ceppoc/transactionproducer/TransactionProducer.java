package com.cognitive.ceppoc.transactionproducer;

import com.cognitive.ceppoc.commons.event.transaction.Transaction;
import com.cognitive.ceppoc.commons.producer.BaseEventProducer;
import com.cognitive.ceppoc.commons.props.CommonProps;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TransactionProducer implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProducer.class);

    public static final int SLEEP_TIME = 100;

    private TransactionCreator transactionCreator;

    private CommonProps commonProps;

    private BaseEventProducer baseEventProducer;

    private Producer<Long, String> eventProducer;

    @Autowired
    public TransactionProducer(TransactionCreator transactionCreator, CommonProps commonProps, BaseEventProducer baseEventProducer) {
        this.transactionCreator = transactionCreator;
        this.commonProps = commonProps;
        this.baseEventProducer = baseEventProducer;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        produceTransactions(10000);
    }

    private void produceTransactions(int totalOperations) {

        eventProducer = baseEventProducer.createProducer();
        for (int i = 0; i < totalOperations; i++) {
            List<Transaction> transactions = transactionCreator.createOperation();
            sendTransactions(transactions);
            sleep();
        }
        eventProducer.close();
    }

    private void sendTransactions(List<Transaction> transactions) {
        transactions.forEach(this::sendTransaction);
    }

    private void sendTransaction(Transaction transaction) {
        ObjectMapper mapper = new ObjectMapper();
        String message;
        try {
            message = mapper.writeValueAsString(transaction);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error on serializing event to send", e);
            return;
        }
        final ProducerRecord<Long, String> record =
                new ProducerRecord<>(commonProps.getTopic(), transaction.getId(), message);

        eventProducer.send(record, (metadata, exception) -> {
            if (metadata != null) {
                logTransaction(transaction);
//                LOGGER.info("sent async record(key={} value={}) meta(partition={}, offset={}) time={}",
//                        record.key(), record.value(), metadata.partition(), metadata.offset(), elapsedTime);
            } else {
                LOGGER.error("Error sending message async", exception);
            }
        });
        eventProducer.flush();

    }

    private void logTransaction(Transaction transaction) {
        LOGGER.info("Transaction {}\t{}\t\t{}\t{}\t{}\t{}\t{}",
                transaction.getId(),
                transaction.getTransactionType(),
                transaction.getInstrumentType(),
                transaction.getInstrumentId(),
                transaction.getAmount(),
                transaction.getOrigin(),
                new Date(transaction.getTime()));
    }

    private void sleep() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            LOGGER.error("Error thread sleep", e);
        }
    }
}
