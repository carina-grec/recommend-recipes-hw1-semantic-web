package com.example.model;

public class UserForm {
    private String name;
    private String surname;
    private String cookingSkillLevel;
    private String preferredCuisineType;

    public UserForm() {
    }

    public User toUser() {
        return new User(null, name, surname, cookingSkillLevel, preferredCuisineType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCookingSkillLevel() {
        return cookingSkillLevel;
    }

    public void setCookingSkillLevel(String cookingSkillLevel) {
        this.cookingSkillLevel = cookingSkillLevel;
    }

    public String getPreferredCuisineType() {
        return preferredCuisineType;
    }

    public void setPreferredCuisineType(String preferredCuisineType) {
        this.preferredCuisineType = preferredCuisineType;
    }
}