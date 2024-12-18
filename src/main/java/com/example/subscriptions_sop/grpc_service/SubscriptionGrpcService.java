package com.example.subscriptions_sop.grpc_service;

import com.example.subscriptions_sop.CancelSubscriptionRequest;
import com.example.subscriptions_sop.ExtendSubscriptionRequest;
import com.example.subscriptions_sop.SubscribeRequest;
import com.example.subscriptions_sop.SubscriptionGrpc;
import com.example.subscriptions_sop.SubscriptionResponse;
import com.example.subscriptions_sop.SubscriptionServiceGrpc;
import com.example.subscriptions_sop.dto.SubDto;
import com.example.subscriptions_sop.dto.SubExtendDto;
import com.example.subscriptions_sop.exceptions.SubscriptionException;
import com.example.subscriptions_sop.model.Subscription;
import com.example.subscriptions_sop.service.SubscriptionService;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionGrpcService extends SubscriptionServiceGrpc.SubscriptionServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionGrpcService.class);
    private SubscriptionService subscriptionService;

    @Override
    public void subscribe(SubscribeRequest request, StreamObserver<SubscriptionResponse> responseObserver) {
        logger.info("subscribe gRPC API called");
        SubDto subDto = new SubDto(request.getSubscriberUsername(), request.getTargetChannelUsername(),
                request.getTier(), request.getDurationInMonths());
        Subscription subscription;
        try {
            subscription = subscriptionService.getSubscriptionDataForSubscribe(subDto);
        } catch (SubscriptionException e) {
            responseObserver.onError(e);
            responseObserver.onCompleted();
            return;
        }
        SubscriptionResponse response = SubscriptionResponse.newBuilder()
                .setSubscription(toGrpcSubscription(subscription))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void extendSubscription(ExtendSubscriptionRequest request,
                                   StreamObserver<SubscriptionResponse> responseObserver) {
        logger.info("extendSubscription gRPC API called");
        SubExtendDto subExtendDto = new SubExtendDto(request.getSubscriberUsername(),
                request.getTargetChannelUsername(), request.getDurationInMonths());
        Subscription subscription;
        try {
            subscription = subscriptionService.getSubscriptionDataForExtend(subExtendDto);
        } catch (SubscriptionException e) {
            responseObserver.onError(e);
            responseObserver.onCompleted();
            return;
        }
        SubscriptionResponse response = SubscriptionResponse.newBuilder()
                .setSubscription(toGrpcSubscription(subscription))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void cancelSubscription(CancelSubscriptionRequest request,
                                   StreamObserver<SubscriptionResponse> responseObserver) {
        logger.info("cancelSubscription gRPC API called");
        Subscription subscription;
        try {
            subscription = subscriptionService.getSubscriptionDataForCancel(request.getSubscriberUsername(),
                    request.getTargetChannelUsername());
        } catch (SubscriptionException e) {
            responseObserver.onError(e);
            responseObserver.onCompleted();
            return;
        }
        SubscriptionResponse response = SubscriptionResponse.newBuilder()
                .setSubscription(toGrpcSubscription(subscription))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private SubscriptionGrpc toGrpcSubscription(Subscription subscription) {
        return SubscriptionGrpc.newBuilder()
                .setSubscriberUsername(subscription.getSubscriber().getUsername())
                .setTargetChannelUsername(subscription.getTargetChannel().getOwner().getUsername())
                .setSubscriptionStartTime(subscription.getSubscriptionStartTime().toString())
                .setSubscriptionEndTime(subscription.getSubscriptionEndTime().toString())
                .setTier(SubscriptionGrpc.SubscriptionTier.forNumber(subscription.getTier().getValue()))
                .setIsActive(subscription.isActive())
                .setPrice(subscription.getPrice())
                .build();
    }

    @Autowired
    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
}
