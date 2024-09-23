package com.SAE.Serveur.Model;

public enum TypePlayer {
    PLAYER1("Player 1"), PLAYER2("Player 2");

    private String text;

    TypePlayer(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TypePlayer{" +
                "text='" + text + '\'' +
                '}';
    }
}
