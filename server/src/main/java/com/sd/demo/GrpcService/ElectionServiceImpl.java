package com.sd.demo.GrpcService;

import io.grpc.stub.StreamObserver;
import election.ElectionServiceGrpc;
import election.ElectionRequest;
import election.ElectionResponse;
import election.CoordinatorAnnouncementRequest;
import election.CoordinatorAnnouncementResponse;

import org.springframework.beans.factory.annotation.Value;

import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
public class ElectionServiceImpl extends ElectionServiceGrpc.ElectionServiceImplBase {

    private final ElectionManager electionManager;
    private final int serverId;
    private final List<Integer> others;

    public ElectionServiceImpl(
        ElectionManager electionManager,
        @Value("${server.id}") int serverId,
        @Value("#{'${election.others}'.split(',')}") List<Integer> others
    ) {
        this.electionManager = electionManager;
        this.serverId = serverId;
        this.others = others;
    }

    @Override
    public void election(ElectionRequest request, StreamObserver<ElectionResponse> responseObserver) {
        int senderId = request.getSenderId();
        boolean alive = serverId > senderId;

        responseObserver.onNext(ElectionResponse.newBuilder().setAlive(alive).build());
        responseObserver.onCompleted();

        if (alive) {
            electionManager.startElection();
        }
    }

    @Override
    public void coordinatorAnnouncement(CoordinatorAnnouncementRequest request, StreamObserver<CoordinatorAnnouncementResponse> responseObserver) {
        int newCoordinatorId = request.getCoordinatorId();
        electionManager.setCoordinator(newCoordinatorId);

        responseObserver.onNext(CoordinatorAnnouncementResponse.newBuilder().setAcknowledged(true).build());
        responseObserver.onCompleted();
    }
}
