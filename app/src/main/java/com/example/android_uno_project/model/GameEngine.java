package com.example.android_uno_project.model;

import android.content.Context;
import com.example.android_uno_project.model.card.Card;
import com.example.android_uno_project.model.card.NormalCard;
import com.example.android_uno_project.model.card.SpecialCard;
import com.example.android_uno_project.model.player.BotPlayer;
import com.example.android_uno_project.model.player.HumanPlayer;
import com.example.android_uno_project.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public final class GameEngine {

    final int HAND_SIZE = 7;

    private final Deck deck = new Deck();
    private final HumanPlayer player = new HumanPlayer();
    private final BotPlayer bot = new BotPlayer();
    private final CardHeap cardHeap = new CardHeap();

    public static String currentColor;

    private final Context context;

    public GameEngine(Context context) {
        this.context = context;
    }

    private void dealFirsthand() {
        for (int i = 0; i < HAND_SIZE; i++) {
            player.drawCard(deck);
            bot.drawCard(deck);
        }

        Card firstCard;

        do{
            firstCard = deck.getCards().remove(deck.getCards().size() - 1);
            if (firstCard instanceof NormalCard) {
                deck.getCards().add(0, firstCard);
            } else {
                cardHeap.addPlayedCard(firstCard);
                currentColor = firstCard.getColor();
            }
        } while (true);
    }

    private void handleSpecialCard(SpecialCard card, Player opponent) {
        switch (card.getEffect()) {
            case "draw_2" : { opponent.drawCard(deck); opponent.drawCard(deck); }
            case "draw_4" : { for (int i = 0; i < 4; i++) opponent.drawCard(deck); }
            case "skip" : SpecialCard.skippedTurn = true;
            case "reverse" : SpecialCard.skippedTurn = true;
        }

        if (card.getColor().equals("n")) { // carta negra -> elegir color aleatorio
            currentColor = Card.getColors()[(int)(Math.random() * 4)];
        } else {
            currentColor = card.getColor();
        }
    }

    public void playHumanTurn(Card selectedCard) {
        Card topCard = cardHeap.getLastPlayedCard();

        if (!selectedCard.isPlayable(topCard, currentColor)) {
            return;
        }

        player.getHand().remove(selectedCard);
        cardHeap.addPlayedCard(selectedCard);

        if (selectedCard instanceof NormalCard) {
            currentColor = selectedCard.getColor();
        }

        if (selectedCard instanceof SpecialCard specialCard) {
            handleSpecialCard(specialCard, bot);
        }
    }

    public void playBotTurn() {
        Card topCard = cardHeap.getLastPlayedCard();
        List<Card> playable = new ArrayList<>();
        for (Card c : bot.getHand()) {
            if (c.isPlayable(topCard, currentColor)) playable.add(c);
        }

        if (!playable.isEmpty()) {
            Card selected = playable.get(0);
            bot.getHand().remove(selected);
            cardHeap.addPlayedCard(selected);

            if (selected instanceof NormalCard) currentColor = selected.getColor();
            if (selected instanceof SpecialCard sc) handleSpecialCard(sc, player);

        } else {
            bot.drawCard(deck);
        }
    }

    public boolean isGameOver() {
        return player.getHand().isEmpty() || bot.getHand().isEmpty();
    }

    public String getWinner() {
        if (player.getHand().isEmpty()) return "Jugador";
        if (bot.getHand().isEmpty()) return "Bot";
        return null;
    }

    public BotPlayer getBot() {
        return bot;
    }
    public Deck getDeck() {
        return deck;
    }
    public CardHeap getCardHeap() {
        return cardHeap;
    }
    public HumanPlayer getPlayer() {
        return player;
    }

}
