package tsyselskyi.andrii.nure;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Button alertButton;
    private Button progressButton;
    private Button datePickerButton;
    private Button timePickerButton;
    private Button customDialogButton;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return windowInsets;
        });

        initializeButtons();
        setupAlertButton();
        setupProgressDialogButton();
        setupDatePickerButton();
        setupTimePickerButton();
        setupCustomDialogButton();
        setupHandlerButton();
        setupRecyclerView();
    }

    private void initializeButtons() {
        alertButton = findViewById(R.id.alertDialog);
        progressButton = findViewById(R.id.progressDialog);
        datePickerButton = findViewById(R.id.dateDialog);
        timePickerButton = findViewById(R.id.timeDialog);
        customDialogButton = findViewById(R.id.customDialog);
    }

    private void setupAlertButton() {
        alertButton.setOnClickListener(v ->
                new AlertDialog.Builder(context)
                        .setTitle("Test Alert")
                        .setMessage("This is a test alert dialog")
                        .setPositiveButton("Accept", (dialog, which) ->
                                Toast.makeText(context, "Alert confirmed", Toast.LENGTH_SHORT).show()
                        )
                        .setNegativeButton("Decline", (dialog, which) ->
                                Toast.makeText(context, "Alert declined", Toast.LENGTH_SHORT).show()
                        )
                        .create()
                        .show()
        );
    }

    private void setupProgressDialogButton() {
        progressButton.setOnClickListener(v -> {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading, please wait...");
            progressDialog.show();
        });
    }

    private void setupDatePickerButton() {
        datePickerButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(context, (view, year, month, dayOfMonth) ->
                    Toast.makeText(context,
                            String.format("Selected: %d-%d-%d", dayOfMonth, month + 1, year),
                            Toast.LENGTH_SHORT).show(),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private void setupTimePickerButton() {
        timePickerButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(context, (view, hourOfDay, minute) ->
                    Toast.makeText(context,
                            String.format("Time: %02d:%02d", hourOfDay, minute),
                            Toast.LENGTH_SHORT).show(),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            ).show();
        });
    }

    private void setupCustomDialogButton() {
        customDialogButton.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.my_custom_dialog);
            dialog.setCancelable(true);
            dialog.findViewById(R.id.okBtn).setOnClickListener(view -> {
                dialog.dismiss();
                Toast.makeText(context, "Custom dialog OK", Toast.LENGTH_SHORT).show();
            });
            dialog.findViewById(R.id.cancelBtn).setOnClickListener(view -> {
                dialog.dismiss();
                Toast.makeText(context, "Custom dialog Cancel", Toast.LENGTH_SHORT).show();
            });
            dialog.show();
        });
    }

    private void setupHandlerButton() {
        findViewById(R.id.handlerDialog).setOnClickListener(v ->
                new Handler(Looper.getMainLooper()).postDelayed(() ->
                                Toast.makeText(context, "3 seconds passed", Toast.LENGTH_SHORT).show(),
                        3000
                )
        );
    }

    private void setupRecyclerView() {
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            dataList.add("RecyclerView item #");
        }
        ((RecyclerView) findViewById(R.id.list)).setAdapter(new ListItemAdapter(dataList));
    }
}
