package com.example.sop_grpc_client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

public class SubscriptionGrpcClient implements AutoCloseable {
    private final ManagedChannel channel;
    private final SubscriptionServiceGrpc.SubscriptionServiceBlockingStub blockingStub;

    public SubscriptionGrpcClient(String host, int port) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = SubscriptionServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public void close() {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted while shutting down channel");
        }
    }

    public static void main(String[] args) {
        try (SubscriptionGrpcClient client = new SubscriptionGrpcClient("localhost", 8088)) {
            client.demonstrateAllOperations();
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void demonstrateAllOperations() {
        try {
            System.out.println("=== Demonstrating Subscription Service Operations ===");
            System.out.println("\n1. Subscribe:");
            try {
                SubscriptionResponse subscriptionResponse = blockingStub.subscribe(
                        SubscribeRequest.newBuilder()
                                .setSubscriberUsername("ivan22")
                                .setTargetChannelUsername("ivan222")
                                .setTier(1)
                                .setDurationInMonths(1)
                                .build()
                );
                System.out.printf("New subscription: %s", subscriptionResponse.toString());
            } catch (StatusRuntimeException e) {
                System.err.println("Failed to subscribe: " + e.getStatus());
            }
            System.out.println("\n2. Extend subscription:");
            try {
                SubscriptionResponse subscriptionExtendResponse = blockingStub.extendSubscription(
                        ExtendSubscriptionRequest.newBuilder()
                                .setSubscriberUsername("ivan22")
                                .setTargetChannelUsername("ivan222")
                                .setDurationInMonths(1)
                                .build()
                );
                System.out.printf("Subscription extended: %s", subscriptionExtendResponse.toString());
            } catch (StatusRuntimeException e) {
                System.err.println("Failed to extend subscription: " + e.getStatus());
            }
            System.out.println("\n3. Cancel subscription:");
            try {
                SubscriptionResponse subscriptionCancelResponse = blockingStub.cancelSubscription(
                        CancelSubscriptionRequest.newBuilder()
                                .setSubscriberUsername("ivan22")
                                .setTargetChannelUsername("ivan222")
                                .build()
                );
                System.out.printf("Subscription canceled: %s", subscriptionCancelResponse.toString());
            } catch (StatusRuntimeException e) {
                System.err.println("Failed to get book: " + e.getStatus());
            }
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Client interrupted: " + e.getMessage());
        }
    }
}
