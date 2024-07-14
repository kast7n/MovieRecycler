package com.example.movierecycler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.receipemanager.R;

public class AddEditMovie extends AppCompatActivity {
    private EditText etTitle, etAuthor, etGenre,etDuration;
    private ImageView ivImage;
    private DatabaseHelper db;
    private String imagePath;
    private String movieId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_movie);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etGenre = findViewById(R.id.etGenre);
        etDuration = findViewById(R.id.etDuration);
        ivImage = findViewById(R.id.ivImage);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent != null) {
            movieId = intent.getStringExtra("id");
            etTitle.setText(intent.getStringExtra("title"));
            etAuthor.setText(intent.getStringExtra("author"));
            etGenre.setText(intent.getStringExtra("genre"));
            etDuration.setText(intent.getStringExtra("duration"));
            imagePath = intent.getStringExtra("imagePath");
            if (imagePath != null && !imagePath.isEmpty()) {
                ivImage.setImageURI(Uri.parse(imagePath));
            }
        }
        ivImage.setOnClickListener(view -> {
            Intent inte = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(inte, 2);
        });

        btnSave.setOnClickListener(view -> saveMovie());
        if(intent != null){
            btnSave.setOnClickListener(view -> editMovie());
        }
        btnCancel.setOnClickListener(view -> finish());
    }

    private void saveMovie() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String genre = etGenre.getText().toString().trim();
        String duration = etDuration.getText().toString().trim();

        Movie movie = new Movie(title, genre, author,duration, imagePath);
        db.insertMovie(movie);

        setResult(RESULT_OK);
        finish();
    }

    private void editMovie() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String genre = etGenre.getText().toString().trim();
        String duration = etDuration.getText().toString().trim();

        Movie movie = new Movie(title, genre, author,duration, imagePath);
        db.deleteMovie(movieId);
        db.insertMovie(movie);

        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            ivImage.setImageURI(imageUri);
            imagePath = imageUri.toString(); // Simplified for example; consider actual path resolution
        }
    }
}
