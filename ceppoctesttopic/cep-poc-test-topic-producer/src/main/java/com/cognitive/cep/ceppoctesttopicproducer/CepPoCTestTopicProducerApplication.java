package com.cognitive.cep.ceppoctesttopicproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan({"com.cognitive.cep", "com.cognitive.ceppoc"})
public class CepPoCTestTopicProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CepPoCTestTopicProducerApplication.class, args);
	}
}
