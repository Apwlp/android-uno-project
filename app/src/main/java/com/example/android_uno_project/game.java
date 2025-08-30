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
        updateCardHeap();
        updateCurrentColor();

        deckImage.setOnClickListener(v -> {
            // Robo carta si no hay jugable
            gameEngine.getPlayer().drawCard(gameEngine.getDeck());
            refreshAdapters();
        });
    }

    private void setupPlayerRecycler() {
        playerAdapter = new CardAdapter(gameEngine.getPlayer().getHand(), card -> {
            // Acción al tocar carta
            gameEngine.playHumanTurn(card);
            refreshAdapters();

            if (gameEngine.isGameOver()) {
                Toast.makeText(this, "¡Ganador: " + gameEngine.getWinner() + "!", Toast.LENGTH_LONG).show();
            } else {
                gameEngine.playBotTurn();
                refreshAdapters();
                updateCardHeap();
                updateCurrentColor();

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
        botAdapter = new CardAdapter(gameEngine.getBot().getHand(), null); // Bot no clickeable
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
        Card lastCard = gameEngine.getCardHeap().getLastPlayedCard();
        int resID = getResources().getIdentifier(lastCard.getImageName(), "drawable", getPackageName());
        cardHeapImage.setImageResource(resID);
    }

    private void updateCurrentColor() {
        currentColorText.setText(gameEngine.currentColor.toUpperCase());
    }
}
