package tsyselskyi.andrii.nure;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GyroscopeActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor gyroscope;
    private TextView gyroscopeText;
    private ImageView imageView;

    private final SensorEventListener gyroscopeListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.values != null) {
                String formattedText = String.format("Gyroscope Data:\nX: %.2f\nY: %.2f\nZ: %.2f",
                        event.values[0], event.values[1], event.values[2]);
                gyroscopeText.setText(formattedText);

                float x = imageView.getX() + event.values[0] * 10;
                float y = imageView.getY() + event.values[1] * 10;
                imageView.setX(x);
                imageView.setY(y);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        initializeComponents();
    }

    private void initializeComponents() {
        gyroscopeText = findViewById(R.id.gyroscopeText);
        imageView = findViewById(R.id.imageView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            if (gyroscope == null) {
                gyroscopeText.setText("Gyroscope not available on this device.");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gyroscope != null) {
            sensorManager.registerListener(gyroscopeListener, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(gyroscopeListener);
        }
    }
}
