package com.example.appfilmes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfilmes.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()

    private val apiKey = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0MDM0MjhlMGYxMjE5ZmI0ZDc0ZjgxNTM5NGI4ZmI5YSIsIm5iZiI6MTc0NTYyNjYzOS4zODYsInN1YiI6IjY4MGMyNjBmZWUxOTk3YmVhZjZlMWQ0MiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.TVYb3rAfk-rc0LKf5zKB9fTTdhsCMeut-eOHNYo8Exg"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Configura o SwipeRefresh
        binding.swipeRefresh.setOnRefreshListener {
            fetchMovies()
        }

        fetchMovies()
    }

    private fun fetchMovies() {
        showLoading(true)

        lifecycleScope.launch {
            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/popular?language=pt-BR&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", apiKey)
                .build()

            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }

            showLoading(false)

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                responseBody?.let { json ->
                    val movieResponseAdapter = moshi.adapter(MovieResponse::class.java)
                    val movieResponse = movieResponseAdapter.fromJson(json)

                    movieResponse?.let { movies ->
                        binding.recyclerView.adapter = MovieAdapter(movies.results)
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Erro ao carregar filmes: ${response.code}", Toast.LENGTH_LONG).show()
            }

            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
}
