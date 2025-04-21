package com.example.movieapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.getViewModel
import com.example.movieapp.ui.viewmodels.DetailViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.movieapp.util.Constants.TMDB_IMAGE_BASE_URL

@Composable
fun DetailScreen(
    movieId: Int,
    onBack: () -> Unit
) {
    val viewModel: DetailViewModel = getViewModel()
    LaunchedEffect(movieId) { viewModel.load(movieId) }

    val detail by viewModel.movieDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            detail?.let { m ->
                Column(modifier = Modifier.padding(16.dp)) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                    AsyncImage(
                        model = TMDB_IMAGE_BASE_URL+"${m.posterPath}",
                        contentDescription = m.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(text = m.title)
                    Spacer(Modifier.height(8.dp))
                    Text(text = m.overview)
                }
            }
        }
    }
}