package com.sd.demo.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sd.demo.DTO.MessageDTO;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import message.MessageServiceGrpc;
import message.PrivateMessageRequest;
import message.MessageResponse;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageServiceGrpc.MessageServiceBlockingStub grpcMessageService;

    public MessageController() {
        // Cria o canal gRPC para localhost na porta 9090
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
            .usePlaintext()
            .build();

        // Cria o stub para chamadas bloqueantes (sincronas)
        this.grpcMessageService = MessageServiceGrpc.newBlockingStub(channel);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO) {
        // Monta a request para gRPC
        PrivateMessageRequest request = PrivateMessageRequest.newBuilder()
            .setSender(messageDTO.getSenderUsername())
            .setReceiver(messageDTO.getReceiverUsername())
            .setContent(messageDTO.getContent())
            .build();

        try {
            // Chama o serviço gRPC para enviar a mensagem
            MessageResponse grpcResponse = grpcMessageService.sendMessage(request);

            // Retorna a resposta do serviço gRPC ao cliente REST
            return ResponseEntity.status(HttpStatus.CREATED).body(grpcResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao enviar mensagem via gRPC: " + e.getMessage());
        }
    }
}
