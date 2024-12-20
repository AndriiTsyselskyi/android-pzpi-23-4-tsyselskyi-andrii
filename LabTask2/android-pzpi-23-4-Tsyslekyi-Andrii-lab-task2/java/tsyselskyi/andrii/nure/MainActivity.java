package tsyselskyi.andrii.nure;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private int red = 0;
    private int green = 0;
    private int blue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View mainView = findViewById(R.id.main);
        View colorPanel = findViewById(R.id.panel);

        configureWindowInsets(mainView);

        initializeColorControls(colorPanel);
    }

    private void configureWindowInsets(View mainView) {
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (view, insets) -> {
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

    private void initializeColorControls(View panel) {
        setupSeekBar(R.id.red_seekbar, value -> updateColorValue(value, ColorComponent.RED, panel));
        setupSeekBar(R.id.green_seekbar, value -> updateColorValue(value, ColorComponent.GREEN, panel));
        setupSeekBar(R.id.blue_seekbar, value -> updateColorValue(value, ColorComponent.BLUE, panel));
    }

    private void setupSeekBar(int seekBarId, SeekBarChangeListener listener) {
        SeekBar seekBar = findViewById(seekBarId);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                listener.onValueChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateColorValue(int value, ColorComponent component, View panel) {
        switch (component) {
            case RED:
                red = value;
                break;
            case GREEN:
                green = value;
                break;
            case BLUE:
                blue = value;
                break;
        }
        updatePanelColor(panel);
    }

    private void updatePanelColor(View panel) {
        panel.setBackgroundColor(Color.rgb(red, green, blue));
    }

    private enum ColorComponent {
        RED, GREEN, BLUE
    }

    @FunctionalInterface
    private interface SeekBarChangeListener {
        void onValueChanged(int value);
    }
}
