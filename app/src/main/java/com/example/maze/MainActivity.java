package com.example.maze;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private int mMazeSize;
    private MazeView mMazeView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Boolean mGameStarted;

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

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mGameStarted = false;
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        if(mGameStarted && se.sensor.equals(mAccelerometer)) {
            float xValue = se.values[0];
            float yValue = se.values[1];
            float zValue = se.values[2];
            mMazeView.addPointToPath(xValue, yValue);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

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
        mGameStarted = true;
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