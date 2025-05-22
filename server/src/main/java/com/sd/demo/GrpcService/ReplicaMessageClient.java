package com.sd.demo.GrpcService;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import message.MessageResponse;
import message.MessageServiceGrpc;
import message.MessageServiceGrpc.MessageServiceBlockingStub;
import message.*;

@Component
public class ReplicaMessageClient {

    private final List<MessageServiceBlockingStub> replicas;

    public ReplicaMessageClient(@Value("${replicas.urls}") List<String> replicaUrls) {
        this.replicas = replicaUrls.stream().map(url -> {
            ManagedChannel channel = ManagedChannelBuilder.forTarget(url).usePlaintext().build();
            return MessageServiceGrpc.newBlockingStub(channel);
        }).toList();
    }

    public void sendMessageToReplicas(MessageResponse message) {
        PrivateMessageRequest request = PrivateMessageRequest.newBuilder()
            .setSender(message.getSender())
            .setReceiver(message.getReceiver())
            .setContent(message.getContent())
            .build();

        for (var replica : replicas) {
            try {
                replica.sendMessage(request);
            } catch (Exception e) {
                // Aqui você pode logar o erro
                e.printStackTrace();
                System.err.println("Erro ao replicar mensagem: " + e.getMessage());
            }
        }
    }
}
