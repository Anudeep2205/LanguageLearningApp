package com.example.llp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class MainMenuActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ImageView gifImageView;
    private TextView menuTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        menuTitle = findViewById(R.id.menuTitle);
        gifImageView = findViewById(R.id.gifImageView);
        Button startButton = findViewById(R.id.startButton);

        animateText();
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void animateText() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(2000);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                menuTitle.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                playGifWithSound();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        menuTitle.startAnimation(fadeIn);
    }

    private void playGifWithSound() {
        gifImageView.setVisibility(View.VISIBLE);
        Glide.with(this).asGif().load(R.drawable.funny_gif).into(gifImageView);
        mediaPlayer = MediaPlayer.create(this, R.raw.funny_sound);
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
