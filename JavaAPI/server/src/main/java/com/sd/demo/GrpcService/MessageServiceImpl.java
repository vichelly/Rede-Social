package com.sd.demo.GrpcService;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import message.MessageResponse;
import message.MessageServiceGrpc.MessageServiceImplBase;
import message.MessageSubscriptionRequest;
import message.PrivateMessageRequest;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class MessageServiceImpl extends MessageServiceImplBase {

    private final Map<String, StreamObserver<MessageResponse>> subscribers = new ConcurrentHashMap<>();

    private final ReplicaMessageClient replicaClient;

    public MessageServiceImpl(ReplicaMessageClient replicaClient) {
        this.replicaClient = replicaClient;
    }

    @Override
    public void sendMessage(PrivateMessageRequest request, StreamObserver<MessageResponse> responseObserver) {
        MessageResponse response = MessageResponse.newBuilder()
                .setSender(request.getSender())
                .setReceiver(request.getReceiver())
                .setContent(request.getContent())
                .setTimestamp(Instant.now().toString())
                .build();

        System.out.println("[sendMessage] Enviando mensagem de " + request.getSender() + " para " + request.getReceiver());

        // Envia a mensagem para o receiver se ele estiver inscrito
        StreamObserver<MessageResponse> receiverObserver = subscribers.get(request.getReceiver());
        if (receiverObserver != null) {
            System.out.println("[sendMessage] Enviando para receiver online: " + request.getReceiver());
            try {
                receiverObserver.onNext(response);
            } catch (Exception e) {
                System.err.println("[sendMessage] Erro enviando para receiver: " + e.getMessage());
            }
        } else {
            System.out.println("[sendMessage] Receiver não inscrito: " + request.getReceiver());
        }

        // Responde ao remetente que enviou a mensagem (ack)
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        // Replica para outros servidores
        replicaClient.sendMessageToReplicas(response);
    }

    @Override
    public void subscribeMessages(MessageSubscriptionRequest request, StreamObserver<MessageResponse> responseObserver) {
        String username = request.getUsername();
        System.out.println("[subscribeMessages] Usuário se inscreveu para mensagens: " + username);

        // Coloca no mapa de inscritos
        subscribers.put(username, responseObserver);

        // Tenta detectar cancelamento da inscrição para limpar o mapa
        if (responseObserver instanceof ServerCallStreamObserver) {
            ServerCallStreamObserver<MessageResponse> serverCallObserver = (ServerCallStreamObserver<MessageResponse>) responseObserver;
            serverCallObserver.setOnCancelHandler(() -> {
                System.out.println("[subscribeMessages] Cancelamento do stream para: " + username);
                subscribers.remove(username);
            });
        }
    }

    public void sendMessageToSubscribers(message.MessageResponse message) {
        StreamObserver<message.MessageResponse> observer = subscribers.get(message.getReceiver());
        if (observer != null) {
            observer.onNext(message);
        }
    }

}
