package tsyselskyi.andrii.nure;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SensorComparisonActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor activeSensor;
    private TextView sensorDataText;

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.values != null) {
                String formattedText = String.format("Sensor Data:\nX: %.3f\nY: %.3f\nZ: %.3f",
                        event.values[0], event.values[1], event.values[2]);
                sensorDataText.setText(formattedText);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_comparison);

        initializeComponents();
    }

    private void initializeComponents() {
        sensorDataText = findViewById(R.id.sensorDataText);
        Spinner sensorSelector = findViewById(R.id.sensorSpinner);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        String[] sensorsList = {"Accelerometer", "Gyroscope", "Magnetometer"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sensorsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sensorSelector.setAdapter(adapter);

        sensorSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                switchSensor(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sensorDataText.setText("No sensor selected.");
            }
        });
    }

    private void switchSensor(int index) {
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorListener);

            switch (index) {
                case 0:
                    activeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    break;
                case 1:
                    activeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                    break;
                case 2:
                    activeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                    break;
                default:
                    activeSensor = null;
            }

            if (activeSensor != null) {
                sensorManager.registerListener(sensorListener, activeSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                sensorDataText.setText("Sensor not available.");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorListener);
        }
    }
}
