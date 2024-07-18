package com.example.movierecycler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MovieDetailActivity extends AppCompatActivity {
    private TextView tvTitle, tvAuthor, tvGenre,tvDuration;
    private ImageView ivImage;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        tvTitle = findViewById(R.id.tvMovieTitle);
        tvAuthor = findViewById(R.id.tvMovieAuthor);
        tvGenre = findViewById(R.id.tvMovieGenre);
        tvDuration = findViewById(R.id.tvMovieDuration);
        ivImage = findViewById(R.id.ivMovieImage);
        Button btnShare = findViewById(R.id.btnShare);
        androidx.appcompat.widget.Toolbar tbDetails = findViewById(R.id.toolbarDetails);

        setSupportActionBar(tbDetails);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }




        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String genre = intent.getStringExtra("genre");
        String duration = intent.getStringExtra("duration");
        String imagePath = intent.getStringExtra("imagePath");

        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvGenre.setText(genre);
        tvDuration.setText(duration);
        if (imagePath != null) {
            ivImage.setImageURI(Uri.parse(imagePath));
        }
        else
        {
            ivImage.setImageResource(R.drawable.default_image);
        }

        btnShare.setOnClickListener(view -> shareMovie(title,genre, author, duration));
    }

    private void shareMovie(String title, String genre, String author,String duration) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = "Movie: " + title + "\n\nGenre:\n" + genre + "\n\nAuthor:\n" + author+ "\n\nDuration:\n" + duration;
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Movie: " + title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}
