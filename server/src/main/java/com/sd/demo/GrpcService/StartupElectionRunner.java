package com.sd.demo.GrpcService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupElectionRunner {

    @Autowired
    private ElectionManager electionManager;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        electionManager.startElection();
    }
}

