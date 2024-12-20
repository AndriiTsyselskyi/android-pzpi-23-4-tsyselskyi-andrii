package tsyselskyi.andrii.nure;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView resultField;
    private EditText numberField;
    private TextView operationField;

    private Double operand = null;
    private String lastOperation = "=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        resultField = findViewById(R.id.resultField);
        numberField = findViewById(R.id.numberField);
        operationField = findViewById(R.id.operationField);
    }

    private void setupListeners() {
        setOperationListeners();
        setNumberListeners();
        findViewById(R.id.clear).setOnClickListener(view -> handleClearClick());
        findViewById(R.id.delete).setOnClickListener(view -> handleDeleteClick());
    }

    private void setOperationListeners() {
        findViewById(R.id.add).setOnClickListener(view -> handleOperationClick("+"));
        findViewById(R.id.sub).setOnClickListener(view -> handleOperationClick("-"));
        findViewById(R.id.mul).setOnClickListener(view -> handleOperationClick("*"));
        findViewById(R.id.div).setOnClickListener(view -> handleOperationClick("/"));
        findViewById(R.id.eq).setOnClickListener(view -> handleOperationClick("="));
    }

    private void setNumberListeners() {
        int[] numberButtons = {
                R.id.n0, R.id.n1, R.id.n2, R.id.n3, R.id.n4,
                R.id.n5, R.id.n6, R.id.n7, R.id.n8, R.id.n9, R.id.comma
        };

        for (int id : numberButtons) {
            findViewById(id).setOnClickListener(view -> handleNumberClick(((Button) view).getText().toString()));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("OPERATION", lastOperation);
        if (operand != null) {
            outState.putDouble("OPERAND", operand);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOperation = savedInstanceState.getString("OPERATION");
        operand = savedInstanceState.getDouble("OPERAND");
        updateUI();
    }

    private void handleNumberClick(String number) {
        numberField.append(number);
        if ("=".equals(lastOperation) && operand != null) {
            operand = null;
        }
    }

    private void handleOperationClick(String operation) {
        String numberInput = numberField.getText().toString();

        if (!TextUtils.isEmpty(numberInput)) {
            numberInput = numberInput.replace(',', '.');
            try {
                performCalculation(Double.valueOf(numberInput), operation);
            } catch (NumberFormatException e) {
                numberField.setText("");
            }
        }

        lastOperation = operation;
        operationField.setText(lastOperation);
    }

    private void performCalculation(Double number, String operation) {
        if (operand == null) {
            operand = number;
        } else {
            if ("=".equals(lastOperation)) {
                lastOperation = operation;
            }

            switch (lastOperation) {
                case "=":
                    operand = number;
                    break;
                case "/":
                    operand = (number == 0) ? 0.0 : operand / number;
                    break;
                case "*":
                    operand *= number;
                    break;
                case "+":
                    operand += number;
                    break;
                case "-":
                    operand -= number;
                    break;
            }
        }

        updateUI();
        numberField.setText("");
    }

    private void updateUI() {
        if (operand != null) {
            resultField.setText(operand.toString().replace('.', ','));
        }
        operationField.setText(lastOperation);
    }

    private void handleClearClick() {
        operand = null;
        lastOperation = "=";
        numberField.setText("");
        resultField.setText("");
        operationField.setText("");
    }

    private void handleDeleteClick() {
        String currentInput = numberField.getText().toString();
        if (!TextUtils.isEmpty(currentInput)) {
            numberField.setText(currentInput.substring(0, currentInput.length() - 1));
        }
    }
}
