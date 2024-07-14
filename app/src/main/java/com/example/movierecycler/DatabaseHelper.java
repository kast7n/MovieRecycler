package com.example.movierecycler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "movies";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_IMAGE_PATH = "imagePath";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_GENRE + " TEXT, " +
                COLUMN_DURATION + " TEXT, " +
                COLUMN_IMAGE_PATH + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, movie.getMovieTitle());
        values.put(COLUMN_AUTHOR, movie.getMovieAuthor());
        values.put(COLUMN_GENRE, movie.getMovieGenre());
        values.put(COLUMN_DURATION, movie.getMovieDuration());
        values.put(COLUMN_IMAGE_PATH, movie.getImagePath());
        return db.insert(TABLE_NAME, null, values);
    }
    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range")  String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range")  String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range")  String author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
                @SuppressLint("Range")  String genre = cursor.getString(cursor.getColumnIndex(COLUMN_GENRE));
                @SuppressLint("Range")  String duration = cursor.getString(cursor.getColumnIndex(COLUMN_DURATION));
                @SuppressLint("Range")  String imagePath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH));
                movies.add(new Movie(id, title,  genre,author,duration, imagePath));
            }
            cursor.close();
        }
        db.close();
        return movies;
    }

    public void deleteMovie(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
        db.close();
    }
}
