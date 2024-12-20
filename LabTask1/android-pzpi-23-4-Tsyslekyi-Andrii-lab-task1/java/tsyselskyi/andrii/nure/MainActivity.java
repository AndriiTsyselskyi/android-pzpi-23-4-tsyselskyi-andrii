package tsyselskyi.andrii.nure;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String ActivityTag = "ActivityLifecycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ActivityTag, "Activity created");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        configureInsets();
    }

    private void configureInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(
                    systemBarsInsets.left,
                    systemBarsInsets.top,
                    systemBarsInsets.right,
                    systemBarsInsets.bottom
            );
            return insets;
        });
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
