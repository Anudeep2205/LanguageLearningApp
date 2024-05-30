package com.example.llp;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private TextView wordPromptTextView;
    private ConstraintLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.rootLayout);
        wordPromptTextView = findViewById(R.id.wordPromptTextView);

        ImageView imageView1 = findViewById(R.id.imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.alma_apple);
                wordPromptTextView.setText("Alma");
            }
        });

        ImageView imageView2 = findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.banan_banana);
                wordPromptTextView.setText("Banán");
            }
        });

        ImageView imageView3 = findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.cseresznye_cherry);
                wordPromptTextView.setText("Cseresznye");
            }
        });

        ImageView imageView4 = findViewById(R.id.imageView4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.szolo_grape);
                wordPromptTextView.setText("Szőlő");
            }
        });

        ImageView imageView5 = findViewById(R.id.imageView5);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.narancs_orange);
                wordPromptTextView.setText("Narancs");
            }
        });

        ImageView imageView6 = findViewById(R.id.imageView6);
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.korte_pear);
                wordPromptTextView.setText("Körte");
            }
        });

        ImageView imageView7 = findViewById(R.id.imageView7);
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.eper_strawberry);
                wordPromptTextView.setText("Eper");
            }
        });

        ImageView imageView8 = findViewById(R.id.imageView8);
        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.gorogdinnye_watermelon);
                wordPromptTextView.setText("Görögdinnye");
            }
        });

        ImageView imageView9 = findViewById(R.id.imageView9);
        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.oszibarack_peach);
                wordPromptTextView.setText("Őszibarack");
            }
        });

        ImageView imageView10 = findViewById(R.id.imageView10);
        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.ananasz_pineapple);
                wordPromptTextView.setText("Ananász");
            }
        });

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScenarioActivity.class);
                intent.putExtra("isQuiz", false);
                startActivity(intent);
            }
        });

        findViewById(R.id.startFruitSelectionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FruitSelectionActivity.class);
                intent.putExtra("isQuiz", true);
                startActivity(intent);
            }
        });
    }

    private void playSound(int resId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, resId);
        mediaPlayer.start();
    }


    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
