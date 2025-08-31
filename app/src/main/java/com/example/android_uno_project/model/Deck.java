package com.example.android_uno_project.model;

import com.example.android_uno_project.model.card.Card;
import com.example.android_uno_project.model.card.NormalCard;
import com.example.android_uno_project.model.card.SpecialCard;
import java.util.Collections;
import java.util.ArrayList;

public final class Deck {

    private final ArrayList<Card> cards = new ArrayList<>();
    private final String[] colors = Card.getColors();
    private final String[] specialEffects = {"skip", "reverse", "draw_2"};
    private final String[] blackEffects = {"wild", "draw_4"};

    public Deck() {
        createDeck();
        shuffleDeck();
    }

    private void createDeck() {
        // Añade un conjunto de cartas 0 para cada color
        for (String color : colors) {
            cards.add(new NormalCard(0, color));
        }

        // Añade cartas 1-9 para cada color
        for (String color : colors) {
            for (int i = 1; i <= 9; i++) {
                cards.add(new NormalCard(i, color));
            }
        }

        // Añade cartas especiales (Skip, Reverse, Draw_2) para cada color
        for (String color : colors) {
            for (String effect : specialEffects) {
                cards.add(new SpecialCard(color, effect));
            }
        }

        // Añade las cartas negras (Wild, Draw_4)
        for (int i = 0; i < 4; i++) {
            cards.add(new SpecialCard("wild"));
            cards.add(new SpecialCard("wild_draw_4"));
        }
    }

    private void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}