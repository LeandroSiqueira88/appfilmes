package com.example.appfilmes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appfilmes.databinding.ItemMovieBinding
import com.example.appfilmes.Movie // ✅ esse import precisa existir!


class MovieAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.textTitle.text = movie.title
        holder.binding.textOverview.text = movie.overview
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
            .into(holder.binding.imagePoster)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = android.content.Intent(context, DetailActivity::class.java)
            intent.putExtra("title", movie.title)
            intent.putExtra("overview", movie.overview)
            intent.putExtra("posterPath", movie.poster_path)
            context.startActivity(intent)
        }
    }
}
