package com.example.android_uno_project.model.card;

public abstract class Card {

    protected static final String[] colors = {"blue", "green", "yellow", "red"};
    protected String color;


    public Card() {
        this.color = "n"; // special Card constructor
    }

    public Card(String color){
        this.color = color; // normal Card constructor
    }

    public static String[] getColors() {
        return colors;
    }

    public String getColor(){ return color; }

    public abstract boolean isPlayable(Card topCard, String currentColor);

    public abstract String getImageName();
}
