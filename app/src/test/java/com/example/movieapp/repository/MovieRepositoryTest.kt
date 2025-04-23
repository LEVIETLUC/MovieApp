
package com.example.movieapp.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.db.MovieDao
import com.example.movieapp.db.MovieDetailDao
import com.example.movieapp.db.MovieDetailEntity
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.db.TmdbDatabase
import com.example.movieapp.network.TmdbService
import com.example.movieapp.network.model.MovieDto
import com.example.movieapp.network.model.MovieDetailResponse
import com.example.movieapp.network.model.MovieResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.HttpException
import retrofit2.Response
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
@Config(manifest = Config.NONE)
class MovieRepositoryTest {

    private lateinit var service: TmdbService
    private lateinit var movieDao: MovieDao
    private lateinit var detailDao: MovieDetailDao
    private lateinit var db: TmdbDatabase
    private lateinit var repository: MovieRepository

    @Before
    fun setUp() {
        // Initialize Dispatchers.Main for unit testing
        Dispatchers.setMain(Dispatchers.Unconfined)
        service = mockk(relaxed = true)
        movieDao = mockk(relaxed = true)
        detailDao = mockk(relaxed = true)
        db = mockk(relaxed = true)
        repository = MovieRepository(service, movieDao, detailDao, db)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun testLoadAndCacheTrendingInsertsMoviesAndDetails() = runBlocking {
        // Given:
        val page = 1
        val dto = MovieDto(
            id = 10,
            posterPath = "/p.jpg",
            title = "T",
            releaseDate = "2025-01-01",
            voteAverage = 8.0
        )
        val apiResponse = MovieResponse(page = page, results = listOf(dto), totalPages = 1)
        coEvery { service.getTrending(page) } returns apiResponse
        coEvery { service.getMovieDetails(dto.id) } returns MovieDetailResponse(
            id = dto.id,
            title = dto.title,
            overview = "O",
            posterPath = dto.posterPath,
            backdropPath = "/b.jpg",
            releaseDate = dto.releaseDate,
            voteAverage = dto.voteAverage,
            voteCount = 1000,
            runtime = 120,
            genres = emptyList(),
            tagline = "A tagline",
            country = emptyList(),
            languages = "en",
            productionCompanies = emptyList(),
            homepage = "https://example.com"
        )

        // When
        val result = repository.loadAndCacheTrending(page)

        // Then
        coVerify { movieDao.insertAll(match<List<MovieEntity>> { list ->
            list.size == 1 && list[0].id == dto.id
        }) }
        coVerify { detailDao.insert(match<MovieDetailEntity> { it.id == dto.id }) }
        assertEquals(1, result.size)
        assertEquals(dto.id, result[0].id)
        assertEquals(dto.title, result[0].title)
        assertEquals(dto.posterPath, result[0].posterPath)
        assertEquals(dto.releaseDate, result[0].releaseDate)
        assertEquals(dto.voteAverage, result[0].voteAverage)
        assertEquals(page, result[0].page)
    }

    @Test
    fun getDetailFallbackToDBOnAPIError() = runBlocking {
        // Given
        val id = 20
        coEvery { service.getMovieDetails(id) } throws HttpException(
            Response.error<Any>(500, ResponseBody.create(null, ""))
        )
        val dbEntity = MovieDetailEntity(
            id = id,
            title = "T",
            overview = "O",
            posterPath = "/p.jpg",
            backdropPath = "/b.jpg",
            releaseDate = "2025-01-01",
            voteAverage = 8.0,
            voteCount = 1000,
            runtime = 120,
            genres = emptyList(),
            tagline = "A tagline",
            country = emptyList(),
            languages = "en",
            productionCompanies = emptyList(),
            homepage = "https://example.com"
        )
        coEvery { detailDao.getById(id) } returns dbEntity

        // When
        val result = repository.getDetail(id)

        // Then
        assertEquals(dbEntity, result)
    }

    @Test(expected = Exception::class)
    fun getDetailThrowsWhenAbsentInDB(): Unit = runBlocking {
        // Given
        val id = 30
        coEvery { service.getMovieDetails(id) } throws Exception("API failed")
        coEvery { detailDao.getById(id) } returns null

        // When & Then: ném exception
        repository.getDetail(id)
    }

    @Test
    fun getMovieDetailReturnsFromDAO() = runBlocking {
        // Given
        val id = 40
        val entity = MovieDetailEntity(
            id = id,
            title = "T",
            overview = "O",
            posterPath = "/p.jpg",
            backdropPath = "/b.jpg",
            releaseDate = "2025-01-01",
            voteAverage = 8.0,
            voteCount = 1000,
            runtime = 120,
            genres = emptyList(),
            tagline = "A tagline",
            country = emptyList(),
            languages = "en",
            productionCompanies = emptyList(),
            homepage = "https://example.com"
        )
        coEvery { detailDao.getById(id) } returns entity

        // When
        val result = repository.getMovieDetail(id)

        // Then
        assertEquals(entity, result)
    }

    @Test
    fun getAllMoviesFromDbReturnsDAOList() = runBlocking {
        // Given
        val list = listOf(MovieEntity(1, "T", "/p.jpg", "2025-01-01", 7.0, 1))
        coEvery { movieDao.getAllMovies() } returns list

        // When
        val result = repository.getAllMoviesFromDb()

        // Then
        assertEquals(list, result)
    }

    @Test
    fun searchMoviesDBReturnsDAOResult() = runBlocking {
        // Given
        val query = "test"
        val list = listOf(MovieEntity(2, "T2", "/p2.jpg", "2025-02-02", 6.5, 1))
        coEvery { movieDao.searchMovies(query) } returns list

        // When
        val result = repository.searchMoviesDB(query)

        // Then
        assertEquals(list, result)
    }

    @Test
    fun getMoviePagingPageReturnsPageData() = runBlocking {
        // Given
        val page = 1
        val fakeData = listOf(MovieEntity(3, "T3", "/p3.jpg", "2025-03-03", 9.0, page))
        val fakeSource = object : PagingSource<Int, MovieEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> =
                LoadResult.Page(data = fakeData, prevKey = null, nextKey = null)
            override fun getRefreshKey(state: PagingState<Int, MovieEntity>) = null
        }
        every { movieDao.pagingSource(page) } returns fakeSource

        // When
        val result = repository.getMoviePagingPage(page)

        // Then
        assertEquals(fakeData, result)
    }

    @Test
    fun searchReturnsMovieResponse() = runBlocking {
        // Given
        val query = "hello"
        val page = 2
        val dto = MovieDto(id = 5, posterPath = "/x.jpg", title = "X", releaseDate = "2025-05-05", voteAverage = 5.5)
        val response = MovieResponse(page, listOf(dto), totalPages = 3)
        coEvery { service.searchMovies(query, page) } returns response

        // When
        val result = repository.search(query, page)

        // Then
        assertEquals(response, result)
    }
}

