package com.example.android_uno_project.model;

import android.content.Context;
import android.widget.Toast;

import com.example.android_uno_project.model.card.Card;
import com.example.android_uno_project.model.card.NormalCard;
import com.example.android_uno_project.model.card.SpecialCard;
import com.example.android_uno_project.model.player.BotPlayer;
import com.example.android_uno_project.model.player.HumanPlayer;
import com.example.android_uno_project.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public final class GameEngine {

    final int HAND_SIZE = 7;

    private final Deck deck = new Deck();
    private final HumanPlayer player = new HumanPlayer();
    private final BotPlayer bot = new BotPlayer();
    private final CardHeap cardHeap = new CardHeap();

    // Interfaz para manejar el cambio de color
    public interface OnColorSelectionListener {
        void onSelectColor(Card card);
    }
    private OnColorSelectionListener colorListener;
    private boolean colorChangePending = false;
    public void setOnColorSelectionListener(OnColorSelectionListener listener) {
        this.colorListener = listener;
    }

    public static String currentColor;
    public int direction = 1;
    public boolean turnSkipped = false;

    private final Context context;

    public GameEngine(Context context) {
        this.context = context;
        dealFirsthand();
    }

    private void dealFirsthand() {
        for (int i = 0; i < HAND_SIZE; i++) {
            player.drawCard(deck);
            bot.drawCard(deck);
        }

        Card firstCard;
        do {
            firstCard = deck.getCards().remove(deck.getCards().size() - 1);
            if (!(firstCard instanceof NormalCard)) {
                deck.getCards().add(0, firstCard);
                Collections.shuffle(deck.getCards());
            }
        } while (!(firstCard instanceof NormalCard));

        cardHeap.addPlayedCard(firstCard);
        currentColor = firstCard.getColor();
    }

    private void handleSpecialCard(SpecialCard card, Player activePlayer, Player opponent) {
        switch (card.getEffect()) {
            case "draw_2": {
                opponent.drawCard(deck);
                opponent.drawCard(deck);
                if (opponent instanceof HumanPlayer) {
                    Toast.makeText(context, "El jugador tomo 2 cartas", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "El bot tomo 2 cartas", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case "wild_draw_4": {
                for (int i = 0; i < 4; i++) {
                    opponent.drawCard(deck);
                }

                if (opponent instanceof HumanPlayer) {
                    Toast.makeText(context, "El jugador tomo 4 cartas", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "El bot tomo 4 cartas", Toast.LENGTH_SHORT).show();
                }
                turnSkipped = true;
                break;
            }
            case "skip":
                turnSkipped = true;
                break;
            case "reverse":
                direction *= -1;
                break;
        }

        if (card.getColor().equals("n")) {
            if (activePlayer instanceof HumanPlayer && colorListener != null) {
                colorChangePending = true;
                colorListener.onSelectColor(card);
            } else {
                currentColor = Card.getColors()[(int) (Math.random() * 4)];
            }
        }
    }

    public void playHumanTurn(Card selectedCard) {
        Card topCard = cardHeap.getLastPlayedCard();

        if (turnSkipped) {
            Toast.makeText(context, "El jugador ha saltado el turno", Toast.LENGTH_SHORT).show();
            turnSkipped = false;
            return;
        }

        if (!selectedCard.isPlayable(topCard, currentColor)) {
            Toast.makeText(context, "El jugador no puede jugar esta carta", Toast.LENGTH_SHORT).show();
            return;
        }

        player.getHand().remove(selectedCard);
        cardHeap.addPlayedCard(selectedCard);

        if (selectedCard instanceof NormalCard) {
            currentColor = selectedCard.getColor();
        }

        if (selectedCard instanceof SpecialCard specialCard) {
            handleSpecialCard(specialCard, player, bot);
        }
    }

    public void playBotTurn() {
        Card topCard = cardHeap.getLastPlayedCard();

        if (turnSkipped) {
            turnSkipped = false;
            return;
        }

        List<Card> playable = new ArrayList<>();
        for (Card c : bot.getHand()) {
            if (c.isPlayable(topCard, currentColor)) playable.add(c);
        }

        if (!playable.isEmpty()) {
            Card selected = playable.get(0);
            bot.getHand().remove(selected);
            cardHeap.addPlayedCard(selected);

            if (selected instanceof NormalCard) currentColor = selected.getColor();
            if (selected instanceof SpecialCard sc) handleSpecialCard(sc, bot, player);
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

    public void setCurrentColor(String color) {
        currentColor = color;
    }

    public boolean isColorChangePending() {
        return colorChangePending;
    }

    public void setColorChangePending(boolean pending) {
        this.colorChangePending = pending;
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