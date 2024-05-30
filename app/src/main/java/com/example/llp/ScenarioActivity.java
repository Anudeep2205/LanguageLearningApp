package com.example.llp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScenarioActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private TextView correctCounter;
    private TextView incorrectCounter;
    private TextView recognizedTextView;
    private ImageView[] imageViews;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private String currentFruitName;
    private Map<String, Integer> fruitMap;
    private List<String> fruitNames;
    private List<String> correctlyIdentifiedFruits;
    private String lastIncorrectFruit;
    private SpeechRecognizer speechRecognizer;
    private ConstraintLayout rootLayout;
    private Button hintButton;
    private Button backButton;
    private MediaPlayer correctMediaPlayer;
    private MediaPlayer incorrectMediaPlayer;

    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        correctCounter = findViewById(R.id.correctCounter);
        incorrectCounter = findViewById(R.id.incorrectCounter);
        recognizedTextView = findViewById(R.id.recognizedTextView);
        rootLayout = findViewById(R.id.rootLayout);
        hintButton = findViewById(R.id.hintButton);
        backButton = findViewById(R.id.backButton);

        imageViews = new ImageView[5];
        imageViews[0] = findViewById(R.id.imageView1);
        imageViews[1] = findViewById(R.id.imageView2);
        imageViews[2] = findViewById(R.id.imageView3);
        imageViews[3] = findViewById(R.id.imageView4);
        imageViews[4] = findViewById(R.id.imageView5);

        initializeFruitMap();
        correctlyIdentifiedFruits = new ArrayList<>();
        displayRandomFruits();

        correctMediaPlayer = MediaPlayer.create(this, R.raw.correct);
        incorrectMediaPlayer = MediaPlayer.create(this, R.raw.incorrect);

        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Log.d("SpeechRecognition", "Ready for speech");
                }

                @Override
                public void onBeginningOfSpeech() {
                    Log.d("SpeechRecognition", "Beginning of speech");
                }

                @Override
                public void onRmsChanged(float rmsdB) {}

                @Override
                public void onBufferReceived(byte[] buffer) {}

                @Override
                public void onEndOfSpeech() {
                    Log.d("SpeechRecognition", "End of speech");
                }

                @Override
                public void onError(int error) {
                    Log.d("SpeechRecognition", "Error: " + error);
                    if (error == SpeechRecognizer.ERROR_NO_MATCH || error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
                        Toast.makeText(ScenarioActivity.this, "Didn't catch that. Try again.", Toast.LENGTH_SHORT).show();
                        recognizedTextView.setText("");
                        rootLayout.setBackgroundColor(Color.RED);
                        incorrectMediaPlayer.start();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rootLayout.setBackgroundColor(Color.WHITE);
                            }
                        }, 1000);
                    }
                }

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null) {
                        String recognizedText = matches.get(0);
                        Log.d("SpeechRecognition", "Recognized text: " + recognizedText);
                        recognizedTextView.setText(recognizedText);
                        checkVoiceInput(recognizedText);
                    }
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    ArrayList<String> partialMatches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (partialMatches != null && !partialMatches.isEmpty()) {
                        recognizedTextView.setText(partialMatches.get(0));
                    }
                }

                @Override
                public void onEvent(int eventType, Bundle params) {}
            });
        } else {
            Toast.makeText(this, "Speech recognition is not available on this device.", Toast.LENGTH_SHORT).show();
        }

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastIncorrectFruit != null) {
                    Toast.makeText(ScenarioActivity.this, "The correct word is: " + lastIncorrectFruit, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScenarioActivity.this, "No hints available.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initializeFruitMap() {
        fruitMap = new HashMap<>();
        fruitMap.put("Alma", R.drawable.apple);
        fruitMap.put("Banán", R.drawable.banana);
        fruitMap.put("Cseresznye", R.drawable.cherry);
        fruitMap.put("Szőlő", R.drawable.grape);
        fruitMap.put("Narancs", R.drawable.orange);

        fruitNames = new ArrayList<>(fruitMap.keySet());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void displayRandomFruits() {
        Collections.shuffle(fruitNames);
        List<String> fruitsToDisplay = fruitNames.subList(0, 5);

        for (int i = 0; i < 5; i++) {
            final String fruitName = fruitsToDisplay.get(i);
            imageViews[i].setImageResource(fruitMap.get(fruitName));
            imageViews[i].setContentDescription(fruitName);
            imageViews[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        currentFruitName = fruitName;
                        startVoiceRecognition();
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        stopVoiceRecognition();
                        v.performClick();
                    }
                    return true;
                }
            });
        }
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hu-HU");
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say the name of the fruit in Hungarian");
        speechRecognizer.startListening(intent);
    }

    private void stopVoiceRecognition() {
        speechRecognizer.stopListening();
    }

    private void checkVoiceInput(String input) {
        if (currentFruitName.equalsIgnoreCase(input)) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            correctlyIdentifiedFruits.add(currentFruitName);
            correctCount++;
            rootLayout.setBackgroundColor(Color.GREEN);
            correctMediaPlayer.start();
            setImageViewToGreenAndDisappear(currentFruitName);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rootLayout.setBackgroundColor(Color.WHITE);
                }
            }, 1000);

            if (correctlyIdentifiedFruits.size() == 5) {
                Toast.makeText(this, "You've correctly identified all fruits!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Incorrect! Try again.", Toast.LENGTH_SHORT).show();
            incorrectCount++;
            lastIncorrectFruit = currentFruitName;
            rootLayout.setBackgroundColor(Color.RED);
            incorrectMediaPlayer.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rootLayout.setBackgroundColor(Color.WHITE);
                }
            }, 1000);
        }
        updateCounters();
    }

    private void setImageViewToGreenAndDisappear(String fruitName) {
        for (ImageView imageView : imageViews) {
            if (fruitName.equals(imageView.getContentDescription().toString())) {
                imageView.setColorFilter(Color.GREEN);
                imageView.setVisibility(View.GONE);
            }
        }
    }

    private void updateCounters() {
        correctCounter.setText("Correct: " + correctCount);
        incorrectCounter.setText("Incorrect: " + incorrectCount);
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        if (correctMediaPlayer != null) {
            correctMediaPlayer.release();
        }
        if (incorrectMediaPlayer != null) {
            incorrectMediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }
}
