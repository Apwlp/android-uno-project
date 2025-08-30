package com.example.android_uno_project.model.card;

import com.example.android_uno_project.model.Deck;
import com.example.android_uno_project.model.GameEngine;
import com.example.android_uno_project.model.player.HumanPlayer;
import com.example.android_uno_project.model.player.Player;

public class SpecialCard extends Card {

    final private String effect;

    private static final String[] effects = {"skip", "reverse"};
    private static final String[] blackEffects = {"wild", "draw_2", "draw_4"};

    public SpecialCard(String color, String effect) {
        super(color);
        this.effect = effect;
    }

    public SpecialCard(String effect) {
        this.color = "n";
        this.effect = effect;
    }

    public static boolean skippedTurn = false;

    public String getImageName() {
        if (color.equals("n")) return "n_" + effect;
        return color + "_" + effect;
    }
    public void playEffect(String effect, Player opponent, Deck deck) {

        if(this.color.equals("n")) {
            if (opponent instanceof HumanPlayer) {
                GameEngine.currentColor = Card.getColors()[(int)(Math.random() * 4)];
            }
        }

        switch (effect) {
            case "draw_2" : {
                for (int i = 0; i < 2; i++) {
                    opponent.drawCard(deck);
                }
                break;
            }
            case "draw_4" : {
                for (int i = 0; i < 4; i++) {
                    opponent.drawCard(deck);
                }
                break;
            }
        }

    }

    public String getEffect() {
        return effect;
    }

    public static String[] getEffects() {
        return effects;
    }

    public static String[] getBlackEffects() {
        return blackEffects;
    }

    @Override
    public String toString() {
        return color.toUpperCase() + effect;

    }

    @Override
    public boolean isPlayable(Card topCard, String currentColor) {
        boolean sameColor = this.color.equals(currentColor);
        boolean sameEffect = topCard instanceof SpecialCard && this.effect.equals(((SpecialCard) topCard).getEffect());
        boolean isBlack = this.color.equals("n");
        return sameColor || sameEffect || isBlack;
    }


}
