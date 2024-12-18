package com.example.subscriptions_sop;

import com.example.subscriptions_sop.grpc_service.SubscriptionGrpcService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SubscriptionsSopApplication {
	public static void main(String[] args) {
		SpringApplication.run(SubscriptionsSopApplication.class, args);
	}
}