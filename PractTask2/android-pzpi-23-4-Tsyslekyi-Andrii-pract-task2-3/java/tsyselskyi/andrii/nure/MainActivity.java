package tsyselskyi.andrii.nure;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String ActivityTag = "MainActivity";
    private Timer timer;
    private TextView timerTextView;
    private TextView counterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ActivityTag, "Activity created");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeViews();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        timerTextView = findViewById(R.id.textViewTimer);
        counterTextView = findViewById(R.id.textViewCounter);
    }

    public void onNextActivityClick(View v) {
        startActivity(new Intent(this, ActivityTwo.class));
    }

    public void onIncreaseCounter(View v) {
        String currentCounterText = counterTextView.getText().toString();
        int counter = TextUtils.isEmpty(currentCounterText) ? 0 : Integer.parseInt(currentCounterText);
        counterTextView.setText(String.valueOf(counter + 1));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        String counter = counterTextView.getText().toString();
        String timer = timerTextView.getText().toString();

        outState.putString("counter", counter);
        outState.putString("timer", timer);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String counter = savedInstanceState.getString("counter", "0");
        String timer = savedInstanceState.getString("timer", "0");

        counterTextView.setText(counter);
        timerTextView.setText(timer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ActivityTag, "Activity started");

        startTimer();

    }

    private void startTimer() {
        timerTextView = findViewById(R.id.textViewTimer);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        }, 0, 1000L);
    }


    private void updateTimer() {
        runOnUiThread(() -> {
            String currentTimerText = timerTextView.getText().toString();
            int timerValue = TextUtils.isEmpty(currentTimerText) ? 0 : Integer.parseInt(currentTimerText);
            timerTextView.setText(String.valueOf(timerValue + 1));
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ActivityTag, "Activity resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ActivityTag, "Activity paused");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ActivityTag, "Activity stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ActivityTag, "Activity destroyed");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(ActivityTag, "Activity restarted");
    }
}
