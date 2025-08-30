package com.example.android_uno_project;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.android_uno_project.model.card.*;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    public interface OnCardClickListener {
        void onCardClick(Card card);
    }

    private List<Card> cards;
    private final OnCardClickListener listener;

    public CardAdapter(List<Card> cards, OnCardClickListener listener) {
        this.cards = cards;
        this.listener = listener;
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

        // Obtener drawable correspondiente a la carta
        int resID = holder.itemView.getContext().getResources()
                .getIdentifier(getDrawableName(card), "drawable", holder.itemView.getContext().getPackageName());

        holder.cardImage.setImageResource(resID);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onCardClick(card);
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    private String getDrawableName(Card card) {
        if (card instanceof NormalCard normalCard) {
            return normalCard.getColor() + normalCard.getDenomination();
        } else if (card instanceof SpecialCard specialCard) {
            return specialCard.getColor().equals("n") ?
                    "n" + specialCard.getEffect() :
                    specialCard.getColor() + specialCard.getEffect();
        }
        return "unknown";
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.cardImage);
        }
    }
}
