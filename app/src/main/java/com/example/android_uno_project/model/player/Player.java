package com.example.android_uno_project.model.player;

import com.example.android_uno_project.model.Deck;
import com.example.android_uno_project.model.card.Card;

import java.util.ArrayList;

public abstract class Player {

    protected ArrayList<Card> hand = new ArrayList<>();

    public void drawCard(Deck deck) {
        hand.add(deck.getCards().remove(0));
        hand.sort((c1,c2) -> Integer.compare(colorOrder(c1.getColor()), colorOrder(c2.getColor())));
    }

    public int colorOrder(String color) {
        return switch (color) {
            case "blue" -> 0;
            case "yellow" -> 1;
            case "red" -> 2;
            case "green" -> 3;
            case "n" -> 4;
            default -> 5;
        };
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public abstract Card playTurn(Card topCard, Player opponent, Deck deck);

    protected void removeCard(int index) {
        hand.remove(index);
        hand.sort((c1,c2) -> Integer.compare(colorOrder(c1.getColor()), colorOrder(c2.getColor())));
    }

}
