package com.example.movierecycler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context context;
    private DatabaseHelper dbHelper;
    public MovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.tvTitle.setText(movie.getMovieTitle());
        holder.tvAuthor.setText(movie.getMovieAuthor());
        holder.tvGenre.setText(movie.getMovieGenre());
        holder.tvDuration.setText(movie.getMovieDuration());

        if (movie.getImagePath() != null) {
            holder.ivRecipeImage.setImageURI(Uri.parse(movie.getImagePath()));
        } else {
            holder.ivRecipeImage.setImageResource(R.drawable.default_image); // Provide a default image if no image is available
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra("title", movie.getMovieTitle());
            intent.putExtra("author", movie.getMovieAuthor());
            intent.putExtra("genre", movie.getMovieGenre());
            intent.putExtra("duration", movie.getMovieDuration());
            intent.putExtra("imagePath", movie.getImagePath());
            context.startActivity(intent);

        });

        holder.btnDelete.setOnClickListener(v -> {
            deleteMovie(position);
        });
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "Movie: " + movie.getMovieTitle() + "\n\nGenre:\n" + movie.getMovieGenre() + "\n\nAuthor:\n" + movie.getMovieAuthor()+ "\n\nDuration:\n" + movie.getMovieDuration();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Movie: " + movie.getMovieTitle());
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                context.startActivity((Intent.createChooser(shareIntent, "Share via")));
            }
        });

        holder.btnYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEARCH);
                intent.setPackage("com.google.android.youtube");
                intent.putExtra("query", movie.getMovieTitle() + " Movie");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.btnEdit.setOnClickListener(v-> {
            Intent intent = new Intent(context, AddEditMovie.class);
            intent.putExtra("id", movie.getId());
            intent.putExtra("title", movie.getMovieTitle());
            intent.putExtra("author", movie.getMovieAuthor());
            intent.putExtra("genre", movie.getMovieGenre());
            intent.putExtra("duration", movie.getMovieDuration());
            intent.putExtra("imagePath", movie.getImagePath());

            context.startActivity(intent);

        });
    }

    private void deleteMovie(int position) {
        Movie movie = movieList.get(position);
        dbHelper.deleteMovie(movie.getId());
        movieList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvAuthor, tvGenre,tvDuration;
        ImageView ivRecipeImage;
        Button btnEdit, btnDelete,btnShare,btnYoutube;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvAuthor = itemView.findViewById(R.id.tvMovieAuthor);
            tvGenre = itemView.findViewById(R.id.tvMovieGenre);
            tvDuration = itemView.findViewById(R.id.tvMovieDuration);
            ivRecipeImage = itemView.findViewById(R.id.ivMovieImage);
            btnDelete = itemView.findViewById(R.id.btnDel);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnShare = itemView.findViewById(R.id.btnShare);
            btnYoutube = itemView.findViewById(R.id.btnYoutube);
        }
    }

}

