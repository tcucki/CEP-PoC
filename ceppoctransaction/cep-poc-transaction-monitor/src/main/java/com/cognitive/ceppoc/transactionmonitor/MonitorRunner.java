package com.cognitive.ceppoc.transactionmonitor;

import com.cognitive.ceppoc.commons.consumer.BaseEventConsumer;
import com.cognitive.ceppoc.commons.props.CommonProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MonitorRunner implements ApplicationListener<ApplicationReadyEvent> {

    private CommonProps commonProps;

    private BaseEventConsumer baseEventConsumer;

    @Autowired
    public MonitorRunner(CommonProps commonProps, BaseEventConsumer baseEventConsumer) {
        this.commonProps = commonProps;
        this.baseEventConsumer = baseEventConsumer;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        new Thread(new TransactionCountMonitor(commonProps, baseEventConsumer)).start();
        new Thread(new TransactionSumMonitor(commonProps, baseEventConsumer)).start();
    }
}
