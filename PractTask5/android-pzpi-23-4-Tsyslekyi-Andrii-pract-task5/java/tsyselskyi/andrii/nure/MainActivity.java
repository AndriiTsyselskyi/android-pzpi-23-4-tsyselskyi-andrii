package tsyselskyi.andrii.nure;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonAccelerometer = findViewById(R.id.buttonAccelerometer);
        Button buttonGyroscope = findViewById(R.id.buttonGyroscope);
        Button buttonSensorComparison = findViewById(R.id.buttonSensorComparison);

        buttonAccelerometer.setOnClickListener(v -> startActivity(new Intent(this, AccelerometerActivity.class)));
        buttonGyroscope.setOnClickListener(v -> startActivity(new Intent(this, GyroscopeActivity.class)));
        buttonSensorComparison.setOnClickListener(v -> startActivity(new Intent(this, SensorComparisonActivity.class)));
    }
}