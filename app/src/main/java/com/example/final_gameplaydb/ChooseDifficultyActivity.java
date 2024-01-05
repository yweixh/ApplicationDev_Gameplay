package com.example.final_gameplaydb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseDifficultyActivity extends AppCompatActivity {

    private Button easyBtn, averageBtn, difficultBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty);

        init();

    }

    private void init() {
        easyBtn = findViewById(R.id.EasyBtn);
        averageBtn = findViewById(R.id.AverageBtn);
        difficultBtn = findViewById(R.id.DiffBtn);

        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseDifficultyActivity.this, EasyGameplay.class);
                startActivity(intent);
                finish();
            }
        });
        averageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseDifficultyActivity.this, AverageGameplay.class);
                startActivity(intent);
                finish();
            }
        });
        difficultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseDifficultyActivity.this, HardGameplay.class);
                startActivity(intent);
                finish();
            }
        });
    }
}