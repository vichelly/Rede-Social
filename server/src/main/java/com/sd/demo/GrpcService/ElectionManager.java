package com.sd.demo.GrpcService;

import election.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ElectionManager {

    private final int serverId;
    private final List<Integer> otherServerIds; // IDs dos outros servidores
    private final AtomicInteger coordinatorId = new AtomicInteger(-1); // Coordenador atual

    public ElectionManager(int serverId, List<Integer> otherServerIds) {
        this.serverId = serverId;
        this.otherServerIds = otherServerIds;
    }

    public void startElection() {
        System.out.println("[ELEICAO] Iniciando eleição por bullying...");

        for (int targetId : otherServerIds) {
            if (targetId > serverId) {
                try {
                    ManagedChannel channel = ManagedChannelBuilder
                            .forAddress("localhost", 50050 + targetId) // exemplo: porta = 50050 + ID
                            .usePlaintext()
                            .build();

                    ElectionServiceGrpc.ElectionServiceBlockingStub stub =
                            ElectionServiceGrpc.newBlockingStub(channel);

                    ElectionResponse response = stub.election(
                            ElectionRequest.newBuilder().setSenderId(serverId).build()
                    );

                    if (response.getAlive()) {
                        System.out.println("Servidor " + targetId + " está vivo. Abortando eleição.");
                        return; // Outro servidor vai ganhar
                    }

                    channel.shutdown();
                } catch (Exception e) {
                    System.out.println("Servidor " + targetId + " não respondeu.");
                }
            }
        }

        // Se chegou aqui, é o novo coordenador
        coordinatorId.set(serverId);
        System.out.println("[ELEICAO] Eu sou o novo coordenador!");

        announceCoordinator();
    }

    private void announceCoordinator() {
        for (int targetId : otherServerIds) {
            try {
                ManagedChannel channel = ManagedChannelBuilder
                        .forAddress("localhost", 50050 + targetId)
                        .usePlaintext()
                        .build();

                ElectionServiceGrpc.ElectionServiceBlockingStub stub =
                        ElectionServiceGrpc.newBlockingStub(channel);

                stub.coordinatorAnnouncement(
                        CoordinatorAnnouncementRequest.newBuilder()
                                .setCoordinatorId(serverId)
                                .build()
                );

                channel.shutdown();
            } catch (Exception e) {
                System.out.println("Falha ao anunciar coordenador para " + targetId);
            }
        }
    }

    public int getCoordinator() {
        return coordinatorId.get();
    }

    public void setCoordinator(int id) {
        System.out.println("[ELEICAO] Coordenador atualizado: " + id);
        coordinatorId.set(id);
    }
}
