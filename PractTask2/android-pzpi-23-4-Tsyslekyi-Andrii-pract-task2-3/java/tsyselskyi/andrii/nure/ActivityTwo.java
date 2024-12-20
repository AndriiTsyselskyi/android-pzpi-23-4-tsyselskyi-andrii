package tsyselskyi.andrii.nure;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityTwo extends AppCompatActivity {

    private static final String ActivityTag = "ActivityTwo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ActivityTag, "Activity created");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_two);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void onFinishClick(View v) {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ActivityTag, "Activity started");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ActivityTag, "Activity resumed");
    }

    @Override
    protected void onPause() {
        Log.i(ActivityTag, "Activity paused");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(ActivityTag, "Activity stopped");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(ActivityTag, "Activity destroyed");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(ActivityTag, "Activity restarted");
    }
}