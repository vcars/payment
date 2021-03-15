package com.example.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(("com.example.payment.entity"))
@EnableJpaRepositories(("com.example.payment.repository"))
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.example.payment.adaptor"})
@SpringBootApplication(scanBasePackages={"com.example.payment"})
public class PaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
	}

}
