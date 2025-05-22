package com.sd.demo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sd.demo.GrpcService.ElectionManager;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Configuration
	public class AppConfig {
		@Bean
		public int serverId(@Value("${server.id}") int id) {
			return id;
		}
	}


    @Value("${election.serverId}")
    private int serverId;

    @Value("#{'${election.others}'.split(',')}")
    private List<String> otherServersString;

    @Override
    public void run(String... args) throws Exception {
        // Converte para Integers
        List<Integer> otherServers = otherServersString.stream()
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        ElectionManager electionManager = new ElectionManager(serverId, otherServers);

        new Thread(() -> {
            try {
                Thread.sleep(3000); // aguarda os outros subirem
                electionManager.startElection();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
