package com.example.model;

import java.util.Arrays;

public class RecipeForm {
    private String title;
    private String cuisine1;
    private String cuisine2;
    private String difficulty;

    public RecipeForm() {
    }

    public Recipe toRecipe() {
        return new Recipe(null, title, Arrays.asList(cuisine1, cuisine2), difficulty);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCuisine1() {
        return cuisine1;
    }

    public void setCuisine1(String cuisine1) {
        this.cuisine1 = cuisine1;
    }

    public String getCuisine2() {
        return cuisine2;
    }

    public void setCuisine2(String cuisine2) {
        this.cuisine2 = cuisine2;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}