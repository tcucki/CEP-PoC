package com.cognitive.ceppoc.transactionmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.cognitive.ceppoc"})
public class CepPoCTestTopicMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CepPoCTestTopicMonitorApplication.class, args);
	}
}
