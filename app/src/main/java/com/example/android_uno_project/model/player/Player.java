package com.example.android_uno_project.model.player;

import com.example.android_uno_project.model.Deck;
import com.example.android_uno_project.model.card.Card;
import java.util.ArrayList;

public abstract class Player {

    protected ArrayList<Card> hand = new ArrayList<>();

    public void drawCard(Deck deck) {
        hand.add(deck.getCards().remove(0));
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public abstract Card playTurn(Card topCard, Player opponent, Deck deck);

    protected void removeCard(int index) {
        hand.remove(index);
    }
}