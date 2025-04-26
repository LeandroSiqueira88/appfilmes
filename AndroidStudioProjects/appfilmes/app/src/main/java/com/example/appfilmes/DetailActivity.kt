package com.example.appfilmes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.appfilmes.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recebe os dados do filme enviados pela MainActivity
        val title = intent.getStringExtra("title")
        val overview = intent.getStringExtra("overview")
        val posterPath = intent.getStringExtra("posterPath")

        // Exibe os dados recebidos
        binding.textTitle.text = title
        binding.textOverview.text = overview

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$posterPath")
            .into(binding.imagePoster)
    }
}
