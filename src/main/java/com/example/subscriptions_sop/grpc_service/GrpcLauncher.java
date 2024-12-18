package com.example.subscriptions_sop.grpc_service;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GrpcLauncher implements CommandLineRunner {

    private final SubscriptionGrpcService subscriptionGrpcService;

    @Autowired
    public GrpcLauncher(SubscriptionGrpcService subscriptionGrpcService) {
        this.subscriptionGrpcService = subscriptionGrpcService;
    }

    @Override
    public void run(String... args) throws Exception {
        Server server = ServerBuilder
                .forPort(8088)
                .addService(subscriptionGrpcService)
                .build();

        server.start();
        System.out.println("GRPC Server started on port 8088");
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.awaitTermination();
    }
}

