package tsyselskyi.andrii.nure;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";

    private static final String SQL_CREATE_TABLE_USER =
            "CREATE TABLE IF NOT EXISTS " + TABLE_USER + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_AGE + " INTEGER)";


    private static final String PREFS_NAME = "app_prefs";
    private static final String PREF_KEY_NAME = "name";
    private static final String PREF_KEY_AGE = "age";

    private final SharedPreferences sharedPreferences;
    private final SQLiteDatabase database;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveUserToPreferences(String name, int age) {
        sharedPreferences.edit()
                .putString(PREF_KEY_NAME, name)
                .putInt(PREF_KEY_AGE, age)
                .apply();
    }

    public String getNameFromPreferences() {
        return sharedPreferences.getString(PREF_KEY_NAME, "");
    }

    public int getAgeFromPreferences() {
        return sharedPreferences.getInt(PREF_KEY_AGE, 0);
    }


    public void insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_AGE, user.getAge());

        long rowId = database.insert(TABLE_USER, null, values);
        if (rowId == -1) {
            Log.e("DatabaseHelper", "Failed to insert user");
        }
    }

    @SuppressLint("Range")
    public List<User> getUsersFromDatabase() {
        List<User> users = new ArrayList<>();

        try (Cursor cursor = database.query(
                TABLE_USER, null, null, null, null, null, null)) {

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                int age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE));

                users.add(new User(id, name, age));
            }

        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error reading users", e);
        }

        return users;
    }
}
