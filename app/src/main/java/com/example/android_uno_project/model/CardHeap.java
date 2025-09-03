package com.example.android_uno_project.model;

import com.example.android_uno_project.model.card.Card;
import java.util.ArrayList;

public class CardHeap {
    private final ArrayList<Card> playedCards = new ArrayList<>();

    public void addPlayedCard(Card card) {
        playedCards.add(card);
    }

    public int getPlayedCardsSize() {
        return playedCards.size();
    }

    public Card getLastPlayedCard() {
        return playedCards.get(playedCards.size() - 1);
    }
}
