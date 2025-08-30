package com.example.android_uno_project.model.card;

public class NormalCard extends Card {

    final private int denomination;

    public NormalCard(int denomination, String color) {
        super(color);
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }

    public String getImageName() {return color.toUpperCase() + "_" + denomination;}

    @Override
    public boolean isPlayable(Card topCard, String currentColor) {
        boolean sameColor = this.color.equals(currentColor);
        boolean sameDenomination = topCard instanceof NormalCard && this.denomination == ((NormalCard) topCard).getDenomination();

        return sameColor || sameDenomination;
    }

}
