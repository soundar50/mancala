package com.bol.mancala.util;

import com.bol.mancala.model.Game;
import com.bol.mancala.model.PlayerRole;
import com.bol.mancala.model.User;
import com.bol.mancala.model.dto.GameDto;
import com.bol.mancala.model.dto.UserDto;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class CopyUtil {

    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getDisplayName(), user.getNoOfGames(), user.getNoOfWins(), user.getNoOfLoses());
    }

    public GameDto toGameDto(Game game) {
        return new GameDto(game.getId(), game.getBoard(), convertToUserDtoMap(game.getPlayers()), game.getPointsScored(), game.isEnded());
    }

    public static Map<PlayerRole, UserDto> convertToUserDtoMap(Map<PlayerRole, User> userMap) {
        return userMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> toUserDto(entry.getValue())
                ));
    }
}
