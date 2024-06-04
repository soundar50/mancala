package com.bol.mancala.repository;

import com.bol.mancala.model.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, UUID> {
}
