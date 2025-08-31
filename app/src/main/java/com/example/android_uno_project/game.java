package com.example.android_uno_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.example.android_uno_project.model.GameEngine;
import com.example.android_uno_project.model.card.Card;
import com.example.android_uno_project.model.card.NormalCard;
import com.example.android_uno_project.model.card.SpecialCard;

public class game extends AppCompatActivity implements GameEngine.OnColorSelectionListener {

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
        gameEngine.setOnColorSelectionListener(this);

        setupPlayerRecycler();
        setupBotRecycler();
        refreshAdapters();

        deckImage.setOnClickListener(v -> {
            gameEngine.getPlayer().drawCard(gameEngine.getDeck());
            refreshAdapters();
        });

        // Bloquear gesto que regresa al home_screen
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (gameEngine.isGameOver()) {
                    // Si acaba el juego, permite salir
                    this.setEnabled(false);
                    game.super.onBackPressed();
                } else {
                    // Si el juego esta en curso, muestra un Pop Up para confirmar salir
                    new AlertDialog.Builder(game.this)
                            .setTitle("Salir del juego")
                            .setMessage("¿Estás seguro de que quieres salir y perder el progreso?")
                            .setPositiveButton("Sí, salir", (dialog, which) -> {
                                this.setEnabled(false);
                                game.super.onBackPressed();
                            })
                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                            .show();
                }
            }
        });
    }

    private void setupPlayerRecycler() {
        playerAdapter = new CardAdapter(gameEngine.getPlayer().getHand(), card -> {
            gameEngine.playHumanTurn(card);
            refreshAdapters();
            if (!gameEngine.isColorChangePending() && !gameEngine.isGameOver()) {
                gameEngine.playBotTurn();
                refreshAdapters();
            }

            if (gameEngine.isGameOver()) {
                Toast.makeText(this, "¡Ganador: " + gameEngine.getWinner() + "!", Toast.LENGTH_LONG).show();
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
            if (resID != 0) {
                cardHeapImage.setImageResource(resID);
            } else {
                cardHeapImage.setImageResource(R.drawable.back_card);
            }
        }
    }

    private void updateCurrentColor() {
        currentColorText.setText(GameEngine.currentColor.toUpperCase());
    }

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
        return "unknown_card";
    }

    @Override
    public void onSelectColor(Card card) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige un color");

        String[] colors = {"Rojo", "Verde", "Azul", "Amarillo"};
        builder.setItems(colors, (dialog, which) -> {
            String selectedColor = "";
            switch (which) {
                case 0:
                    selectedColor = "red";
                    break;
                case 1:
                    selectedColor = "green";
                    break;
                case 2:
                    selectedColor = "blue";
                    break;
                case 3:
                    selectedColor = "yellow";
                    break;
            }
            gameEngine.setCurrentColor(selectedColor);
            gameEngine.setColorChangePending(false);
            gameEngine.playBotTurn();
            refreshAdapters();
        });
        builder.show();
    }
}
