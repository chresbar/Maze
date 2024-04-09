package com.example.maze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ParametersActivity extends AppCompatActivity {

    SeekBar mMazeSizeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parameters);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mMazeSizeInput = findViewById(R.id.mazeSize);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            int gameLevel = intent.getIntExtra("mazeSize", 0);
            mMazeSizeInput.setProgress(gameLevel - 10);
        }
    }

    public void save(View v) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("mazeSize", mMazeSizeInput.getProgress() + 10);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void back(View v) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}