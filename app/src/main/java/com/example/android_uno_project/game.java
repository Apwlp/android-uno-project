package com.example.android_uno_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android_uno_project.model.GameEngine;
import com.example.android_uno_project.model.card.Card;
import com.example.android_uno_project.model.card.NormalCard;
import com.example.android_uno_project.model.card.SpecialCard;

public class game extends AppCompatActivity{

    private RecyclerView playerCardsRecycler, botCardsRecycler;
    private ImageView cardHeapImage, deckImage;
    private TextView currentColorText;
    private GameEngine gameEngine;
    private CardAdapter playerAdapter, botAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        playerCardsRecycler = findViewById(R.id.playerCards);
        botCardsRecycler = findViewById(R.id.botCards);
        cardHeapImage = findViewById(R.id.cardHeap);
        deckImage = findViewById(R.id.deck);
        currentColorText = findViewById(R.id.currentColor);

        gameEngine = new GameEngine(this);

        setupPlayerRecycler();
        setupBotRecycler();
        refreshAdapters(); // Call refreshAdapters here to show the initial state

        deckImage.setOnClickListener(v -> {
            gameEngine.getPlayer().drawCard(gameEngine.getDeck());
            refreshAdapters();
        });
    }

    private void setupPlayerRecycler() {
        playerAdapter = new CardAdapter(gameEngine.getPlayer().getHand(), card -> {
            gameEngine.playHumanTurn(card);
            refreshAdapters();

            if (gameEngine.isGameOver()) {
                Toast.makeText(this, "¡Ganador: " + gameEngine.getWinner() + "!", Toast.LENGTH_LONG).show();
            } else {
                gameEngine.playBotTurn();
                refreshAdapters();

                if (gameEngine.isGameOver()) {
                    Toast.makeText(this, "¡Ganador: " + gameEngine.getWinner() + "!", Toast.LENGTH_LONG).show();
                }
            }
        });

        playerCardsRecycler.setAdapter(playerAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        playerCardsRecycler.setLayoutManager(layoutManager);
    }

    private void setupBotRecycler() {
        botAdapter = new CardAdapter(gameEngine.getBot().getHand(), null);
        botCardsRecycler.setAdapter(botAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        botCardsRecycler.setLayoutManager(layoutManager);
    }

    private void refreshAdapters() {
        playerAdapter.setCards(gameEngine.getPlayer().getHand());
        botAdapter.setCards(gameEngine.getBot().getHand());
        updateCardHeap();
        updateCurrentColor();
    }

    private void updateCardHeap() {
        if (gameEngine.getCardHeap().getPlayedCardsSize() > 0) {
            Card lastCard = gameEngine.getCardHeap().getLastPlayedCard();
            String drawableName = getCardImageName(lastCard);
            int resID = getResources().getIdentifier(drawableName, "drawable", getPackageName());
            if (resID != 0) { // Check if resource was found
                cardHeapImage.setImageResource(resID);
            } else {
                // Log an error or set a default image
                cardHeapImage.setImageResource(R.drawable.back_card); // Replace with your card back image
            }
        }
    }

    private void updateCurrentColor() {
        currentColorText.setText(gameEngine.currentColor.toUpperCase());
    }

    // Helper method to get the correct lowercase drawable name
    private String getCardImageName(Card card) {
        if (card instanceof NormalCard) {
            NormalCard normalCard = (NormalCard) card;
            return (normalCard.getColor() + "_" + normalCard.getDenomination()).toLowerCase();
        } else if (card instanceof SpecialCard) {
            SpecialCard specialCard = (SpecialCard) card;
            if (specialCard.getColor().equals("n")) {
                return ("n_" + specialCard.getEffect()).toLowerCase();
            } else {
                return (specialCard.getColor() + "_" + specialCard.getEffect()).toLowerCase();
            }
        }
        return "unknown_card"; // Default name for an unknown card
    }
}