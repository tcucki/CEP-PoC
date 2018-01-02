package com.cognitive.ceppoc.ceppoctesttopicconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.cognitive.ceppoc"})
public class CepPoCTestTopicConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CepPoCTestTopicConsumerApplication.class, args);
    }
}
