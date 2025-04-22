package com.example.movieapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.koin.androidx.compose.getViewModel
import com.example.movieapp.ui.viewmodels.DetailViewModel
import com.example.movieapp.util.Constants.TMDB_IMAGE_BASE_URL
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.max

@SuppressLint("DefaultLocale")
@Composable
fun DetailScreen(
    movieId: Int,
    onBack: () -> Unit
) {
    val viewModel: DetailViewModel = getViewModel()
    LaunchedEffect(movieId) { viewModel.load(movieId) }

    val movie by viewModel.movieDetail.collectAsState()
    val loading by viewModel.isLoading.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF141824)
    ) {
        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            movie?.let { m ->
                Column(modifier = Modifier.fillMaxSize()) {
                    // Top hero section: backdrop + poster
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                    ) {
                        AsyncImage(
                            model = "$TMDB_IMAGE_BASE_URL${m.backdropPath}",
                            contentDescription = "Backdrop",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .padding(16.dp)
                                .size(36.dp)
                                .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(18.dp))
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        AsyncImage(
                            model = "$TMDB_IMAGE_BASE_URL${m.posterPath}",
                            contentDescription = "Poster",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(120.dp)
                                .height(160.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = (-16).dp, y = 60.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }

                    // Title, rating, website
                    Row(
                        modifier = Modifier
                            .width(280.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = m.title,
                                color = Color.Red,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            m.tagline?.takeIf { it.isNotBlank() }?.let { tag ->
                                Text(
                                    text = tag,
                                    color = Color.LightGray,
                                    fontStyle = FontStyle.Italic,
                                    maxLines = 2,
                                    fontSize = 12.sp
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 0.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                            Text(
                                text = String.format("%.1f/10", m.voteAverage),
                                color = Color(0xFFFFC107),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "(${m.voteCount} votes)",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                            IconButton(
                                onClick = {},
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(36.dp)
                            ) {
                                Icon(
                                    Icons.Default.Share,
                                    contentDescription = "Explore",
                                    tint = Color.LightGray
                                )
                            }
                            }
                        }


                    }

                    // Metadata
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        InfoRow(label = "Release Date", value = m.releaseDate.toString())
                        InfoRow(label = "Runtime", value = "${m.runtime} minutes")
                        InfoRow(label = "Genres", value = m.genres?.joinToString(", "){it.name} ?: "N/A")
                        InfoRow(label = "Country", value = m.country?.joinToString(", ") ?: "N/A")
                        InfoRow(label = "Languages", value = m.languages?.toString()?: "N/A")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Overview
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Overview:",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = m.overview,
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Production Companies
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Production Companies",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            items(m.productionCompanies?.size ?: 0) { idx ->
                                CompanyItem(name = m.productionCompanies?.get(idx)?.name ?: "N/A",
                                    logoPath = m.productionCompanies?.get(idx)?.logoPath)

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = "$label: ", color = Color.White, fontSize = 14.sp)
        Text(text = value, color = Color.LightGray, fontSize = 12.sp)
    }
}

@Composable
private fun CompanyItem(name: String, logoPath: String? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            if (logoPath != null) {
                AsyncImage(
                    model = "$TMDB_IMAGE_BASE_URL$logoPath",
                    contentDescription = "Company Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f)
                )
            } else {
                Text(
                    text = name.first().toString(),
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp).width(4.dp))
        Text(
            text = name,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    // Preview with sample data
    DetailScreen(
        movieId = 0,
        onBack = {}
    )
}