package com.sd.demo.GrpcService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.grpc.stub.StreamObserver;
import post.PostResponse;
import org.springframework.stereotype.Component;

@Component
public class PostStreamManager {

    private final List<StreamObserver<PostResponse>> observers = new CopyOnWriteArrayList<>();

    public void addSubscriber(StreamObserver<PostResponse> observer) {
        observers.add(observer);
    }

    public void notifySubscribers(PostResponse response) {
        for (StreamObserver<PostResponse> observer : observers) {
            try {
                observer.onNext(response);
            } catch (Exception e) {
                observers.remove(observer);
            }
        }
    }
}
