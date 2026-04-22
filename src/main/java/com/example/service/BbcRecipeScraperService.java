package com.example.service;

import com.example.model.Recipe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BbcRecipeScraperService {

    private static final String BBC_URL = "https://www.bbcgoodfood.com/recipes/collection/budget-autumn";

    private static final List<String> CUISINES = List.of(
            "Italian", "Asian", "Indian", "Mexican",
            "French", "Mediterranean", "American", "European"
    );

    private static final List<String> DIFFICULTIES = List.of(
            "Beginner", "Intermediate", "Advanced"
    );

    private final Random random = new Random();

    public List<Recipe> scrapeRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(BBC_URL)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Set<String> titles = extractUniqueTitles(doc);

            int id = 1;
            for (String title : titles) {
                if (title == null || title.isBlank()) {
                    continue;
                }

                recipes.add(new Recipe(
                        String.valueOf(id++),
                        title.trim(),
                        getTwoDifferentRandomCuisines(),
                        getRandomDifficulty()
                ));

                if (recipes.size() >= 20) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipes;
    }

    private Set<String> extractUniqueTitles(Document doc) {
        Set<String> titles = new LinkedHashSet<>();
        Elements links = doc.select("a[href*='/recipes/']");

        for (Element link : links) {
            String text = cleanTitle(link.text().trim());
            if (isValidRecipeTitle(text)) {
                titles.add(text);
            }
        }

        return titles;
    }

    private String cleanTitle(String text) {
        return text.replace("App only", "").trim();
    }

    private boolean isValidRecipeTitle(String text) {
        if (text == null || text.isBlank()) return false;
        if (text.length() < 4 || text.length() > 100) return false;

        List<String> invalidFragments = List.of(
                "Budget autumn recipes",
                "ratings",
                "Search",
                "Subscribe",
                "Good Food team",
                "Healthy",
                "Vegetarian",
                "Easy"
        );

        for (String invalid : invalidFragments) {
            if (text.equalsIgnoreCase(invalid) || text.contains(invalid)) {
                return false;
            }
        }

        return true;
    }

    private List<String> getTwoDifferentRandomCuisines() {
        List<String> shuffled = new ArrayList<>(CUISINES);
        Collections.shuffle(shuffled, random);
        return List.of(shuffled.get(0), shuffled.get(1));
    }

    private String getRandomDifficulty() {
        return DIFFICULTIES.get(random.nextInt(DIFFICULTIES.size()));
    }
}