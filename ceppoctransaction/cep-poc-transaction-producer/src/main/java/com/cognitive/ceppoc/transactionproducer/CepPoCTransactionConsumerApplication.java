package com.cognitive.ceppoc.transactionproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.cognitive.ceppoc"})
public class CepPoCTransactionConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CepPoCTransactionConsumerApplication.class, args);
	}
}
