package com.example.crud.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ClockSyncService {

    private int localClock = 0;
    private final List<String> otherServers;

    public ClockSyncService(List<String> otherServers) {
        this.otherServers = otherServers;
    }

    public void syncClock() {
        // Simulação do algoritmo de Berkeley
        int avgTime = calculateAverageTimeFromServers();
        this.localClock = avgTime; // Atualiza o relógio local com a média calculada
    }

    private int calculateAverageTimeFromServers() {
        // Este é apenas um esboço do cálculo, você deve implementar a comunicação com os servidores para obter seus tempos.
        int totalTime = 0;
        for (String server : otherServers) {
            totalTime += getTimeFromServer(server); // Supondo que você tem uma forma de pegar o tempo do servidor
        }
        return totalTime / otherServers.size();
    }

    private int getTimeFromServer(String server) {
        // Você pode fazer uma chamada REST ou gRPC para obter o tempo de outros servidores
        return 1000; // valor simulado para o tempo do servidor
    }
}
