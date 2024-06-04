package com.bol.mancala.repository;

import com.bol.mancala.model.Lobby;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LobbyRepository extends ReactiveMongoRepository<Lobby, UUID> {
}
