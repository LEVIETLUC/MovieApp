package com.example.movieapp.screens

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.ui.screens.TrendingScreen
import com.example.movieapp.ui.viewmodels.TrendingViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module


@RunWith(JUnit4::class)

class TrendingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: TrendingViewModel
    private val moviesFlow = MutableStateFlow<List<MovieEntity>>(emptyList())
    private val isLoadingFlow = MutableStateFlow(false)

    @Before
    fun setup() {
        stopKoin()
        viewModel = mockk(relaxed = true) {
            every { movies } returns moviesFlow
            every { isLoading } returns isLoadingFlow
            every { loadNextPage() } returns Unit
        }

        startKoin {
            modules(module {
                single { viewModel }
            })
        }

    }


    @Test
    fun displayMoviesAndClickItem() {
        val movies = listOf(
            MovieEntity(
                id = 1,
                title = "Movie One",
                posterPath = "/poster1.jpg",
                releaseDate = "2023-01-01",
                voteAverage = 7.5,
                page = 1
            ),
            MovieEntity(
                id = 2,
                title = "Movie Two",
                posterPath = "/poster2.jpg",
                releaseDate = "2022-01-01",
                voteAverage = 8.0,
                page = 1
            )
        )
        var clickedMovieId: Int? = null

        moviesFlow.value = movies

        composeTestRule.setContent {
            TrendingScreen(
                onMovieClick = { clickedMovieId = it },
                onSearchClick = {}
            )
        }

        composeTestRule.onRoot().printToLog("TAG")
        composeTestRule.waitUntil {
            composeTestRule.onAllNodes(hasText("Movie One")).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Movie One").assertIsDisplayed()
        composeTestRule.onNodeWithText("Movie Two").assertIsDisplayed()

        composeTestRule.onNodeWithText("Movie One").performClick()

        assert(clickedMovieId == 1)
    }

    @Test
    fun displayLoadingIndicator() {
        isLoadingFlow.value = true

        composeTestRule.setContent {
            TrendingScreen(
                onMovieClick = {},
                onSearchClick = {}
            )
        }

        composeTestRule.onRoot().printToLog("TAG")
        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun clickSearchButtonTriggersCallback() {
        var clicked = false
        composeTestRule.setContent {
            TrendingScreen(
                onMovieClick = {},
                onSearchClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Search").performClick()
        assert(clicked)
    }

    @Test
    fun emptyMovieListDoesNotCrash() {
        composeTestRule.setContent {
            moviesFlow.value = emptyList()
            TrendingScreen(
                onMovieClick = {},
                onSearchClick = {}
            )
        }

        composeTestRule.onAllNodes(hasText("")).assertCountEquals(0)
    }

}