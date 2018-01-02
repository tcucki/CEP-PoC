package com.cognitive.ceppoc.transactionmonitor;

import com.cognitive.ceppoc.commons.consumer.BaseEventConsumer;
import com.cognitive.ceppoc.commons.props.CommonProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TransactionCountMonitor extends BaseTransactionMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionCountMonitor.class);

    public TransactionCountMonitor(CommonProps commonProps, BaseEventConsumer baseEventConsumer) {
        super(commonProps, baseEventConsumer, "TX_COUNT");
    }

    @Override
    protected void processEvent(Map<String, String> event) {
        LOGGER.info("Card id {} had {} purchases on time window", event.get("INSTRUMENTID"), event.get("PURCHASECOUNT"));
    }
}
