package com.example.android_uno_project.model.card;

import com.example.android_uno_project.model.Deck;
import com.example.android_uno_project.model.GameEngine;
import com.example.android_uno_project.model.player.HumanPlayer;
import com.example.android_uno_project.model.player.Player;

public class SpecialCard extends Card {

    final private String effect;

    private static final String[] effects = {"skip", "reverse", "draw_2"};
    private static final String[] blackEffects = {"wild", "wild_draw_4"};

    public SpecialCard(String color, String effect) {
        super(color);
        this.effect = effect;
    }

    public SpecialCard(String effect) {
        this.color = "n";
        this.effect = effect;
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
    public boolean isPlayable(Card topCard, String currentColor) {
        boolean sameColor = this.color.equals(currentColor);
        boolean sameEffect = topCard instanceof SpecialCard && this.effect.equals(((SpecialCard) topCard).getEffect());
        boolean isBlack = this.color.equals("n");
        return sameColor || sameEffect || isBlack;
    }
}
