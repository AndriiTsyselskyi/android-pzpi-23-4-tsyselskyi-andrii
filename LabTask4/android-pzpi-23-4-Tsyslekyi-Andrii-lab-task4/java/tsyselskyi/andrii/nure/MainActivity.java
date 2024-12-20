package tsyselskyi.andrii.nure;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {

    private static final int IMAGE_PICK_REQUEST_CODE = 1;
    private RecyclerView notesRecyclerView;
    private NoteAdapter notesAdapter;
    private final ArrayList<Note> allNotes = new ArrayList<>();
    private final ArrayList<Note> filteredNotes = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private Uri selectedImageUri;
    private boolean sortByImportanceAscending = true;
    private boolean sortByTitleAscending = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
        setupRecyclerView();
        setupAddNoteButton();
    }

    private void initializeComponents() {
        notesRecyclerView = findViewById(R.id.recyclerView);
        dbHelper = new DatabaseHelper(this);
    }

    private void setupRecyclerView() {
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NoteAdapter(this, filteredNotes, this);
        notesRecyclerView.setAdapter(notesAdapter);
    }

    private void setupAddNoteButton() {
        findViewById(R.id.addNoteButton).setOnClickListener(v -> navigateToAddNoteActivity());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNotes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            toggleSortByImportance();
            return true;
        } else if (item.getItemId() == R.id.action_sort_az) {
            toggleSortByTitle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterNotes(String query) {
        filteredNotes.clear();
        String lowerCaseQuery = query.toLowerCase();
        for (Note note : allNotes) {
            if (note.getDescription().toLowerCase().contains(lowerCaseQuery) ||
                    note.getTitle().toLowerCase().contains(lowerCaseQuery)) {
                filteredNotes.add(note);
            }
        }
        notesAdapter.notifyDataSetChanged();
    }

    private void toggleSortByImportance() {
        List<Note> targetList = filteredNotes.isEmpty() ? allNotes : filteredNotes;
        if (sortByImportanceAscending) {
            Collections.sort(targetList, (n1, n2) -> Integer.compare(n1.getImportance(), n2.getImportance()));
            showToast("Sorted by importance (ascending)");
        } else {
            Collections.sort(targetList, (n1, n2) -> Integer.compare(n2.getImportance(), n1.getImportance()));
            showToast("Sorted by importance (descending)");
        }
        sortByImportanceAscending = !sortByImportanceAscending;
        notesAdapter.notifyDataSetChanged();
    }

    private void toggleSortByTitle() {
        List<Note> targetList = filteredNotes.isEmpty() ? allNotes : filteredNotes;
        if (sortByTitleAscending) {
            Collections.sort(targetList, (n1, n2) -> n1.getTitle().compareTo(n2.getTitle()));
        } else {
            Collections.sort(targetList, (n1, n2) -> n2.getTitle().compareTo(n1.getTitle()));
        }
        sortByTitleAscending = !sortByTitleAscending;
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshNotesList();
    }

    private void refreshNotesList() {
        allNotes.clear();
        allNotes.addAll(dbHelper.getAllNotes());
        filteredNotes.clear();
        filteredNotes.addAll(allNotes);
        notesAdapter.notifyDataSetChanged();
    }

    private void navigateToAddNoteActivity() {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNoteClick(Note note) {
        navigateToEditNoteActivity(note);
    }

    @Override
    public void onEditNoteClick(Note note, int position) {
        navigateToEditNoteActivity(note);
    }

    private void navigateToEditNoteActivity(Note note) {
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("note_id", note.getId());
        intent.putExtra("note_title", note.getTitle());
        intent.putExtra("note_description", note.getDescription());
        intent.putExtra("note_importance", note.getImportance());
        intent.putExtra("note_date_time", note.getDateTime());
        intent.putExtra("note_image_uri", note.getImageUri() != null ? note.getImageUri().toString() : null);
        startActivity(intent);
    }

    @Override
    public void onDeleteNoteClick(Note note, int position) {
        dbHelper.deleteNote(note.getId());
        allNotes.remove(note);
        filteredNotes.remove(note);
        notesAdapter.notifyDataSetChanged();
        showToast("Note deleted successfully");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
