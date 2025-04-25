package com.example.movieapp.ui.screens

import android.R.attr.contentDescription
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.getViewModel
import com.example.movieapp.ui.viewmodels.TrendingViewModel

@Composable
fun TrendingScreen(
    onMovieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,

) {
    val viewModel: TrendingViewModel = getViewModel()
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // LazyListState for detecting scroll
    val listState = rememberLazyListState()

    // Trigger load next page when scrolled to the bottom
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .map { info ->
                val total = info.totalItemsCount
                val lastIndex = info.visibleItemsInfo.lastOrNull()?.index ?: 0
                lastIndex >= total - 1
            }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                viewModel.loadNextPage()
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Trending Today")
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(2.dp)
        ) {
            items(movies.size) { index ->
                val movie = movies[index]
                MovieRow(movie = movie) { onMovieClick(movie.id) }
            }

            item {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .testTag("LoadingIndicator") // hoặc
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun MovieRow(
    movie: com.example.movieapp.db.MovieEntity,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(6.dp)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w185${movie.posterPath}",
            contentDescription = movie.title,
            modifier = Modifier.size(80.dp)
        )
        Spacer(Modifier.width(4.dp))
        Column {
            Text(text = movie.title)
            Text(text = movie.releaseDate.take(4))
            Text(text = "⭐ ${movie.voteAverage}")
        }
    }
}