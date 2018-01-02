package com.cognitive.ceppoc.ceppoctesttopicconsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerRunner implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerRunner.class);

    private EventConsumer eventConsumer;

    @Autowired
    public ConsumerRunner(EventConsumer eventConsumer) {
        this.eventConsumer = eventConsumer;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOGGER.info("********************** RUNNING CONSUMER ************************");
        eventConsumer.runConsumer();
    }
}
