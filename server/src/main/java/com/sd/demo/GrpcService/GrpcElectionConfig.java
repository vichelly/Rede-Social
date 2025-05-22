package com.sd.demo.GrpcService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class GrpcElectionConfig {

    @Value("${election.server-id}")
    private int serverId;

    @Value("${election.other-servers}")
    private String otherServersRaw;


    @Bean
    public ElectionManager electionManager() {
        List<Integer> otherServerIds = Arrays.stream(otherServersRaw.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        return new ElectionManager(serverId, otherServerIds);
    }
}