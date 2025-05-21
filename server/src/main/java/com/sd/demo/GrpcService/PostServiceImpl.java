package com.sd.demo.GrpcService;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import post.CreatePostRequest;
import post.PostResponse;
import post.PostServiceGrpc;
import post.SubscribeRequest;

@GrpcService
public class PostServiceImpl extends PostServiceGrpc.PostServiceImplBase {

    @Autowired
    private PostStreamManager streamManager;

    @Override
    public void createPost(CreatePostRequest request, StreamObserver<PostResponse> responseObserver) {
        PostResponse response = PostResponse.newBuilder()
            .setUsername(request.getUsername())
            .setContent(request.getContent())
            .setTimestamp(Long.toString(System.currentTimeMillis()))
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        // Notifica os inscritos
        streamManager.notifySubscribers(response);
    }

    @Override
    public void subscribePosts(SubscribeRequest request, StreamObserver<PostResponse> responseObserver) {
        streamManager.addSubscriber(responseObserver);
        System.out.println("Novo assinante via gRPC: " + request.getUsername());
    }
}