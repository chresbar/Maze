package com.example.maze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private int mMazeSize;

    private MazeView mMazeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mMazeSize = 20;

        mMazeView = new MazeView(this);

        LinearLayout ll = findViewById(R.id.main);
        ll.addView(mMazeView);
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null && intent.getExtras() != null) {
                        int mazeSize = intent.getIntExtra("mazeSize", 20);
                        mMazeSize = mazeSize;
                    }
                }
            });

    public void playGame(View v) {

    }

    public void generateMaze(View v) {
        mMazeView.setMazeGenerator(new MazeGenerator(mMazeSize));
    }

    public void parameters(View v) {
        Intent intent = new Intent(this, ParametersActivity.class);
        intent.putExtra("mazeSize", mMazeSize);
        mStartForResult.launch(intent);
    }

    public void quit(View v) {
        this.finish();
    }
}