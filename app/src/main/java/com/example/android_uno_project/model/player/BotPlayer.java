package com.example.android_uno_project.model.player;

import android.os.Build;

import com.example.android_uno_project.model.card.Card;
import com.example.android_uno_project.model.*;

import java.util.List;

public class BotPlayer extends Player {

    @Override
    public Card playTurn(Card topCard, Player opponent, Deck deck) {
        List<Card> playableCards = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            playableCards = hand.stream()
                    .filter(card -> card.isPlayable(topCard, GameEngine.currentColor))
                    .toList();
        }

        assert playableCards != null;
        if (playableCards.isEmpty()) return null;

        Card selected = playableCards.get(0);
        hand.remove(selected);
        return selected;
    }

}
