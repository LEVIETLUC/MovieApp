//package com.example.movieapp.ui.screens
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.CircularProgressIndicator
//import androidx.compose.material.Icon
//import androidx.compose.material.IconButton
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.unit.dp
//import coil.compose.AsyncImage
//import org.koin.androidx.compose.getViewModel
//import com.example.movieapp.ui.viewmodels.DetailViewModel
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.tooling.preview.PreviewParameter
//import com.example.movieapp.util.Constants.TMDB_IMAGE_BASE_URL
//
//@Composable
//fun DetailScreen(
//    movieId: Int,
//    onBack: () -> Unit
//) {
//    val viewModel: DetailViewModel = getViewModel()
//    LaunchedEffect(movieId) { viewModel.load(movieId) }
//
//    val detail by viewModel.movieDetail.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        if (isLoading) {
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//        } else {
//            detail?.let { m ->
//                Column(modifier = Modifier.padding(16.dp)) {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = null)
//                    }
//                    AsyncImage(
//                        model = TMDB_IMAGE_BASE_URL+"${m.posterPath}",
//                        contentDescription = m.title,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(300.dp),
//                        contentScale = ContentScale.Crop
//                    )
//                    Spacer(Modifier.height(16.dp))
//                    Text(text = m.title)
//                    Spacer(Modifier.height(8.dp))
//                    Text(text = m.overview)
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MyViewPreview() {
//    DetailScreen(
//        movieId = 12344,
//        onBack = {}
//    )
//}

package com.example.movieapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.koin.androidx.compose.getViewModel
import com.example.movieapp.ui.viewmodels.DetailViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.tooling.preview.Preview
import com.example.movieapp.util.Constants.TMDB_IMAGE_BASE_URL

@Composable
fun DetailScreen(
    movieId: Int,
    onBack: () -> Unit
) {
    val viewModel: DetailViewModel = getViewModel()
    LaunchedEffect(movieId) {
        viewModel.load(movieId)
    }
    val detail by viewModel.movieDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF141824))) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            detail?.let { movie ->
                Column(modifier = Modifier.fillMaxSize()) {
                    // Backdrop and Poster section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        // Backdrop image
                        AsyncImage(
                            model = "$TMDB_IMAGE_BASE_URL${movie.backdropPath}",
                            contentDescription = "Movie backdrop",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp),
                            contentScale = ContentScale.Crop
                        )

                        // Poster image
                        AsyncImage(
                            model = "$TMDB_IMAGE_BASE_URL${movie.posterPath}",
                            contentDescription = "Movie poster",
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 16.dp)
                                .width(180.dp)
                                .height(250.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        // Title and tagline
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 16.dp, bottom = 50.dp)
                        ) {
                            Text(
                                text = movie.title ?: "Sinners",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = movie.tagline ?: "Dance with the devil.",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }

                        // Rating
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 16.dp, bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "7.5/10",
                                color = Color(0xFFFFD700),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = " (181 votes)",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Movie details section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Visit website button
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { /* Visit website */ },
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(bottom = 16.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1A73E8))
                            ) {
                                Text("Visit Official Website", color = Color.White)
                            }
                        }

                        // Movie metadata
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                text = "Release Date: April 16, 2025",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Runtime: 138 minutes",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Genres: Drama, Horror, Music",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Country: US",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Languages: English",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }

                        // Overview
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                text = "Overview:",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = movie.overview ?: "Trying to leave their troubled lives behind, twin brothers return to their hometown to start again, only to discover that an even greater evil is waiting to welcome them back.",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }

                        // Production Companies
                        Column(modifier = Modifier.padding(vertical = 16.dp)) {
                            Text(
                                text = "Production Companies",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ProductionCompanyItem("Warner Bros. Pictures")
                                ProductionCompanyItem("Proximity Media")
                                ProductionCompanyItem("Domain Entertainment")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductionCompanyItem(name: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.first().toString(),
                color = Color.White,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 2
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        movieId = 12344,
        onBack = {}
    )
}