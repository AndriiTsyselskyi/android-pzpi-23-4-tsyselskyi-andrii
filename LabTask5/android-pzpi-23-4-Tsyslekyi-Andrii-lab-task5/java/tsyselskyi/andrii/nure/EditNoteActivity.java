package tsyselskyi.andrii.nure;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class EditNoteActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText titleText, descriptionText;
    private Spinner importanceSpinner;
    private Button updateButton, selectImageButton, deleteButton;
    private ImageView noteImageView;
    private Uri imageUri;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);
        importanceSpinner = findViewById(R.id.importanceSpinner);
        updateButton = findViewById(R.id.updateButton);
        selectImageButton = findViewById(R.id.selectImageButton);
        deleteButton = findViewById(R.id.deleteButton);
        noteImageView = findViewById(R.id.noteImageView);

        long id = getIntent().getLongExtra("note_id", 0);

        String title = getIntent().getStringExtra("note_title");
        String description = getIntent().getStringExtra("note_description");
        int importance = getIntent().getIntExtra("note_importance", 0);
        String dateTime = getIntent().getStringExtra("note_date_time");
        String imageUriString = getIntent().getStringExtra("note_image_uri");
        Uri imageUri2 = null;
        if (imageUriString != null) {
            imageUri2 = Uri.parse(imageUriString);
        }


        note = new Note(id, title, description, importance, dateTime, imageUri2);

        titleText.setText(note.getTitle());
        descriptionText.setText(note.getDescription());

        imageUri = note.getImageUri();
        noteImageView.setImageURI(imageUri);
        selectImageButton.setOnClickListener(v -> openGallery());

        updateButton.setOnClickListener(v -> updateNote());
        deleteButton.setOnClickListener(v -> deleteNote());


        String[] importanceLevels = {"Low", "Medium", "High"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, importanceLevels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importanceSpinner.setAdapter(adapter);
        importanceSpinner.setSelection(note.getImportance());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
    private Uri saveImageLocally(Uri sourceUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            String fileName = "note_image_" + UUID.randomUUID().toString() + ".jpg";
            OutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            outputStream.close();
            inputStream.close();

            return Uri.fromFile(getFileStreamPath(fileName));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = saveImageLocally(data.getData());
            if (imageUri != null) {
                noteImageView.setImageURI(imageUri);
            } else {
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateNote() {
        String title = titleText.getText().toString().trim();
        String description = descriptionText.getText().toString().trim();
        int importance = importanceSpinner.getSelectedItemPosition();
        String imageUriString = (imageUri != null) ? imageUri.toString() : "";
        String dateTime = getCurrentDateTime();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            int rowsUpdated = dbHelper.updateNote(note.getId(), title, description, importance, dateTime, imageUriString);

            if (rowsUpdated > 0) {
                Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error updating note", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void deleteNote() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.deleteNote(note.getId());

        Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
