package com.cognitive.cep.ceppoctesttopicproducer;

import com.cognitive.ceppoc.commons.props.CommonProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PoCRunner implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoCRunner.class);

    private CommonProps commonProps;

    private EventProducer eventProducer;

    @Autowired
    public PoCRunner(CommonProps commonProps, EventProducer eventProducer) {
        this.commonProps = commonProps;
        this.eventProducer = eventProducer;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOGGER.info("Running Test Topic PoC event producer");
        eventProducer.runProducerAsync(5);
    }
}
