package com.example.android_uno_project.model;

import com.example.android_uno_project.model.card.Card;
import com.example.android_uno_project.model.card.NormalCard;
import com.example.android_uno_project.model.card.SpecialCard;
import java.util.Collections;
import java.util.ArrayList;


public final class Deck {
    public Deck() {
        createDeck();
        shuffleDeck();
    }

    String[] colors = Card.getColors();
    String[] effects = SpecialCard.getEffects();
    String[] blackEffects = SpecialCard.getBlackEffects();

    ArrayList<Card> Cards = new ArrayList<>();

    private void createDeck() {
        // Add normal Cards
        for (String color : colors) {
            for (int i = 0; i < 10; i++) {
                Cards.add(new NormalCard(i, color));
            }
        }
        // Add special Cards
        for (String color : colors) {
            for (String effect : effects) {
                for (int i = 0; i < 2; i++) {
                    Cards.add(new SpecialCard(color, effect));
                }
            }
        }
        // Add special black Cards
        for (String effect : blackEffects) {
            for (int i = 0; i < 2 ; i++) {
                Cards.add(new SpecialCard(effect));
            }
        }
    }

    private void shuffleDeck() {
        // Shuffle the Deck
        Collections.shuffle(Cards);
    }

    public ArrayList<Card> getCards() {
        return Cards;
    }

}
