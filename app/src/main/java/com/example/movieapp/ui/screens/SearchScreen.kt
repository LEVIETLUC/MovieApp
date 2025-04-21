package com.example.movieapp.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import org.koin.androidx.compose.getViewModel
import com.example.movieapp.ui.viewmodels.SearchViewModel

@Composable
fun SearchScreen(
    onMovieClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val viewModel: SearchViewModel = getViewModel()
    val results by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var text by remember { mutableStateOf("") }

    Column {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
            TextField(
                value = text,
                onValueChange = { new ->
                    text = new
                    viewModel.onQueryChanged(new)
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search movies...") }
            )
        }
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
        LazyColumn {
            items(results) { movie ->
                movie?.let { MovieRow(movie, onClick = { onMovieClick(it.id) }) }
            }
        }
    }
}

@Preview
@Composable
fun MyViewPrevie3() {
    SearchScreen(
        onMovieClick = {},
        onBack = {}
    )
}