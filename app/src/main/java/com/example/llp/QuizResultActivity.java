package com.example.llp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class QuizResultActivity extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        resultTextView = findViewById(R.id.resultTextView);
        int correctCount = getIntent().getIntExtra("correctCount", 0);
        resultTextView.setText("Your Score: " + correctCount + "/5");
    }

    public void backToMainMenu(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
