package com.zwash.auth.serviceImpl;

import com.zwash.common.grpc.UserServiceGrpc;
import com.zwash.common.pojos.User;
import com.zwash.auth.controller.UserController;
import com.zwash.auth.exceptions.UserIsNotFoundException;
import com.zwash.auth.service.UserService;
import com.zwash.common.grpc.TokenRequest;
import com.zwash.common.grpc.UserIdRequest;
import com.zwash.common.grpc.UserResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@GrpcService 
public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {



	Logger logger = LoggerFactory.getLogger(UserServiceGrpcImpl.class);
			
    @Autowired
    private UserService userService;

    @Override
    public void getUserById(UserIdRequest request, StreamObserver<UserResponse> responseObserver) {
        logger.info("Received getUserById request with id: {}", request.getId());

        try {
            User user = userService.getUser(request.getId());

            UserResponse response = UserResponse.newBuilder()
                    .setId(user.getId())
                    .setFirstName(user.getFirstName() != null ? user.getFirstName() : "")
                    .setLastName(user.getLastName() != null ? user.getLastName() : "")
                    .setUsername(user.getUsername() != null ? user.getUsername() : "")
                    .setDateOfBirth(user.getDateOfBirth() != null ? user.getDateOfBirth() : "")
                    .setActive(user.isActive() != null && user.isActive())
                    .setAdmin(user.isAdmin() != null && user.isAdmin())
                    .setToken(user.getToken() != null ? user.getToken() : "")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("Error fetching user by id: {}", request.getId(), e);
            responseObserver.onError(
                io.grpc.Status.INTERNAL
                    .withDescription("Internal server error")
                    .augmentDescription(e.getMessage())
                    .asRuntimeException()
            );
        }
    }

    @Override
    public void getUserFromToken(TokenRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            User user = userService.getUserFromToken(request.getToken());

            UserResponse response = UserResponse.newBuilder()
                    .setId(user.getId())
                    .setFirstName(user.getFirstName() != null ? user.getFirstName() : "")
                    .setLastName(user.getLastName() != null ? user.getLastName() : "")
                    .setUsername(user.getUsername() != null ? user.getUsername() : "")
                    .setDateOfBirth(user.getDateOfBirth() != null ? user.getDateOfBirth() : "")
                    .setActive(user.isActive() != null && user.isActive())
                    .setAdmin(user.isAdmin() != null && user.isAdmin())
                    .setToken(user.getToken() != null ? user.getToken() : "")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("Error fetching user from token", e);
            responseObserver.onError(
                io.grpc.Status.INTERNAL
                    .withDescription("Internal server error")
                    .augmentDescription(e.getMessage())
                    .asRuntimeException()
            );
        }
    }
}
