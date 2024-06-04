package com.bol.mancala.model;

public enum PlayerRole {
    ONE,
    TWO;

    public PlayerRole opponent() {
        return switch (this) {
            case ONE -> TWO;
            case TWO -> ONE;
        };
    }
}
