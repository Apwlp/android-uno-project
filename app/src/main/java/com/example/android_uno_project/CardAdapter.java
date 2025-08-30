package com.example.android_uno_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_uno_project.model.card.Card;
import com.example.android_uno_project.model.card.NormalCard;
import com.example.android_uno_project.model.card.SpecialCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    public interface OnCardClickListener {
        void onCardClick(Card card);
    }

    private List<Card> cards;
    private final OnCardClickListener listener;
    private final Map<String, Integer> cardImageMap = new HashMap<>();

    public CardAdapter(List<Card> cards, OnCardClickListener listener) {
        this.cards = cards;
        this.listener = listener;
        populateImageMap();
    }

    public void setCards(List<Card> newCards) {
        this.cards = newCards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cards.get(position);

        String imageName = getCardImageName(card);
        Integer resId = cardImageMap.get(imageName);

        if (resId != null) {
            holder.cardImage.setImageResource(resId);
        } else {
            // Maneja el caso de que la imagen no se encuentre
            // Puedes usar una imagen de comodín o un log de error
            holder.cardImage.setImageResource(R.drawable.back_card);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onCardClick(card);
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    private String getCardImageName(Card card) {
        if (card instanceof NormalCard) {
            NormalCard normalCard = (NormalCard) card;
            return normalCard.getColor() + "_" + normalCard.getDenomination();
        } else if (card instanceof SpecialCard) {
            SpecialCard specialCard = (SpecialCard) card;
            if (specialCard.getColor().equals("n")) {
                return "n_" + specialCard.getEffect();
            } else {
                return specialCard.getColor() + "_" + specialCard.getEffect();
            }
        }
        return "unknown"; // Nombre de imagen por defecto
    }

    private void populateImageMap() {
        // Colores
        String[] colors = {"blue", "green", "red", "yellow"};

        // Cartas normales (0-9)
        for (String color : colors) {
            for (int i = 0; i <= 9; i++) {
                String name = color + "_" + i;
                try {
                    int resId = R.drawable.class.getDeclaredField(name).getInt(null);
                    cardImageMap.put(name, resId);
                } catch (Exception e) {
                    // Log the error to help with debugging missing drawables
                    System.err.println("Drawable not found for: " + name);
                    e.printStackTrace();
                }
            }
        }

        // Cartas de efectos
        String[] effects = {"skip", "reverse"};
        for (String color : colors) {
            for (String effect : effects) {
                String name = color + "_" + effect;
                try {
                    int resId = R.drawable.class.getDeclaredField(name).getInt(null);
                    cardImageMap.put(name, resId);
                } catch (Exception e) {
                    System.err.println("Drawable not found for: " + name);
                    e.printStackTrace();
                }
            }
        }

        // Cartas negras (wild y draw_4)
        String[] blackEffects = {"wild", "draw_2", "draw_4"};
        for (String effect : blackEffects) {
            String name = "n_" + effect;
            try {
                int resId = R.drawable.class.getDeclaredField(name).getInt(null);
                cardImageMap.put(name, resId);
            } catch (Exception e) {
                System.err.println("Drawable not found for: " + name);
                e.printStackTrace();
            }
        }
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.cardImage);
        }
    }
}