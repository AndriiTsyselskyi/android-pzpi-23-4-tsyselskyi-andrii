package tsyselskyi.andrii.nure;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import tsyselskyi.andrii.nure.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private UserAdapter adapter;
    private ActivityMainBinding binding;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);

        setupRecyclerView();
        setupListeners();

        loadUserFromPreferences();
    }

    private void setupRecyclerView() {
        adapter = new UserAdapter();
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);
        updateUserList();
    }

    private void setupListeners() {
        binding.write.setOnClickListener(v -> saveConfig());
        binding.read.setOnClickListener(v -> displayConfig());
        binding.add.setOnClickListener(v -> addUser());
    }

    private void saveConfig() {
        String configData = binding.config.getText().toString().trim();
        if (!configData.isEmpty()) {
            writeToFile(configData);
            binding.config.setText("");
            showToast("Config saved!");
        } else {
            showToast("Config is empty!");
        }
    }

    private void displayConfig() {
        binding.result.setText(readFromFile());
    }

    private void addUser() {
        String name = binding.name.getText().toString().trim();
        String ageStr = binding.age.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty()) {
            showToast("Name or Age cannot be empty");
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            User user = new User(null, name, age);
            databaseHelper.insertUser(user);

            databaseHelper.saveUserToPreferences(name, age);

            updateUserList();

            binding.name.setText("");
            binding.age.setText("");

            showToast("User added!");
        } catch (NumberFormatException e) {
            showToast("Invalid age format");
        }
    }

    private void loadUserFromPreferences() {
        String name = databaseHelper.getNameFromPreferences();
        int age = databaseHelper.getAgeFromPreferences();

        if (!name.isEmpty() && age != 0) {
            binding.name.setText(name);
            binding.age.setText(String.valueOf(age));
        }
    }

    private void updateUserList() {
        List<User> users = databaseHelper.getUsersFromDatabase();
        adapter.updateUserList(users);
    }

    private void writeToFile(String data) {
        try (OutputStreamWriter writer = new OutputStreamWriter(openFileOutput("config.txt", MODE_PRIVATE))) {
            writer.write(data);
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Failed to save config");
        }
    }

    private String readFromFile() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("config.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Failed to read config");
        }
        return content.toString().trim();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
