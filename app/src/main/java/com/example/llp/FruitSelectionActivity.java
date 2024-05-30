package com.example.llp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FruitSelectionActivity extends AppCompatActivity {

    private TextView fruitNameTextView;
    private TextView correctCounter;
    private TextView incorrectCounter;
    private ImageView[] imageViews;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private String currentFruitName;
    private Map<String, Integer> fruitMap;
    private List<String> fruitNames;
    private List<String> selectedFruits;
    private Set<String> correctlyIdentifiedFruits;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_selection);

        fruitNameTextView = findViewById(R.id.fruitNameTextView);
        correctCounter = findViewById(R.id.correctCounter);
        incorrectCounter = findViewById(R.id.incorrectCounter);

        imageViews = new ImageView[5];
        imageViews[0] = findViewById(R.id.imageView1);
        imageViews[1] = findViewById(R.id.imageView2);
        imageViews[2] = findViewById(R.id.imageView3);
        imageViews[3] = findViewById(R.id.imageView4);
        imageViews[4] = findViewById(R.id.imageView5);

        correctlyIdentifiedFruits = new HashSet<>();
        initializeFruitMap();
        selectRandomFruits();
        displayCurrentFruit();
    }

    private void initializeFruitMap() {
        fruitMap = new HashMap<>();
        fruitMap.put("Körte", R.drawable.pear);
        fruitMap.put("Eper", R.drawable.strawberry);
        fruitMap.put("Görögdinnye", R.drawable.watermelon);
        fruitMap.put("Őszibarack", R.drawable.peach);
        fruitMap.put("Ananász", R.drawable.pineapple);

        fruitNames = new ArrayList<>(fruitMap.keySet());
    }

    private void selectRandomFruits() {
        Collections.shuffle(fruitNames);
        selectedFruits = fruitNames.subList(0, 5);
    }

    private void displayCurrentFruit() {
        if (correctlyIdentifiedFruits.size() >= 5) {
            Toast.makeText(this, "You have correctly identified all the fruits!", Toast.LENGTH_LONG).show();
            finish(); // End the activity
            return;
        }

        currentFruitName = selectedFruits.get(currentIndex % selectedFruits.size());
        fruitNameTextView.setText(currentFruitName);

        Collections.shuffle(fruitNames);
        List<String> fruitsToDisplay = fruitNames.subList(0, 5);
        if (!fruitsToDisplay.contains(currentFruitName)) {
            fruitsToDisplay.set((int) (Math.random() * 5), currentFruitName);
        }

        for (int i = 0; i < 5; i++) {
            final String fruitName = fruitsToDisplay.get(i);
            imageViews[i].setImageResource(fruitMap.get(fruitName));
            imageViews[i].setContentDescription(fruitName);
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(v);
                }
            });
        }
    }

    private void checkAnswer(View view) {
        String selectedFruit = view.getContentDescription().toString();
        if (selectedFruit.equals(currentFruitName)) {
            correctCount++;
            correctlyIdentifiedFruits.add(currentFruitName);
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            currentIndex++;
            displayCurrentFruit();
        } else {
            incorrectCount++;
            Toast.makeText(this, "Incorrect! Try again.", Toast.LENGTH_SHORT).show();
        }
        updateCounters();
    }

    private void updateCounters() {
        correctCounter.setText("Correct: " + correctCount);
        incorrectCounter.setText("Incorrect: " + incorrectCount);
    }
}
