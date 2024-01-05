package com.example.final_gameplaydb;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    Button doneBtn;
    TextView finalScore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        doneBtn = findViewById(R.id.done_button);
        finalScore = findViewById(R.id.final_score);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreActivity.this, ChooseDifficultyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.my_anim);
        finalScore.setAnimation(anim);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SCORE")) {
            int score = intent.getIntExtra("SCORE", 0);

            // Display the score in the TextView
            finalScore.setText("" + score);
        }
    }
}