package tsyselskyi.andrii.nure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMPORTANCE = "importance";
    public static final String COLUMN_DATE_TIME = "dateTime";
    public static final String COLUMN_IMAGE_URI = "imageUri";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder createTableQuery = new StringBuilder();
        createTableQuery.append("CREATE TABLE ").append(TABLE_NOTES).append(" (")
                .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(COLUMN_TITLE).append(" TEXT, ")
                .append(COLUMN_DESCRIPTION).append(" TEXT, ")
                .append(COLUMN_IMPORTANCE).append(" INTEGER, ")
                .append(COLUMN_DATE_TIME).append(" TEXT, ")
                .append(COLUMN_IMAGE_URI).append(" TEXT)");

        db.execSQL(createTableQuery.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public long addNote(String title, String description, int importance, String dateTime, String imageUri) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, title);
            values.put(COLUMN_DESCRIPTION, description);
            values.put(COLUMN_IMPORTANCE, importance);
            values.put(COLUMN_DATE_TIME, dateTime);
            values.put(COLUMN_IMAGE_URI, imageUri);

            return db.insert(TABLE_NOTES, null, values);
        }
    }

    public List<Note> getAllNotes() {
        List<Note> notesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                    int importance = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMPORTANCE));
                    String dateTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_TIME));
                    String imageUriString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI));

                    Uri imageUri = Uri.parse(imageUriString);
                    Note note = new Note(id, title, description, importance, dateTime, imageUri);
                    notesList.add(note);

                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return notesList;
    }

    public int updateNote(long id, String title, String description, int importance, String dateTime, String imageUri) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, title);
            values.put(COLUMN_DESCRIPTION, description);
            values.put(COLUMN_IMPORTANCE, importance);
            values.put(COLUMN_DATE_TIME, dateTime);
            values.put(COLUMN_IMAGE_URI, imageUri);

            return db.update(TABLE_NOTES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        }
    }

    public void deleteNote(long id) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.delete(TABLE_NOTES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        }
    }

    public void deleteAllNotes() {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.delete(TABLE_NOTES, null, null);
        }
    }
}
