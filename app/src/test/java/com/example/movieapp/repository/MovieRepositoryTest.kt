//import android.util.Log
//import com.example.movieapp.db.MovieDao
//import com.example.movieapp.db.MovieDetailDao
//import com.example.movieapp.db.TmdbDatabase
//import com.example.movieapp.network.TmdbService
//import com.example.movieapp.repository.MovieRepository
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import org.junit.Before
//import com.example.movieapp.db.MovieDetailEntity
//import com.example.movieapp.db.MovieEntity
//import com.example.movieapp.network.model.MovieDetailResponse
//import com.example.movieapp.network.model.MovieResponse
//import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.test.runTest
//import org.junit.Assert.assertEquals
//import org.mockito.kotlin.mock
//import org.mockito.kotlin.whenever
//import org.mockito.kotlin.verify
//import org.mockito.kotlin.doReturn
//
//import org.junit.Test
//import org.mockito.Mockito.`when`
//
//@ExperimentalCoroutinesApi
//class MovieRepositoryTest {
//
//    private val service = mock<TmdbService>()
//    private val movieDao = mock<MovieDao>()
//    private val detailDao = mock<MovieDetailDao>()
//    private val database = mock<TmdbDatabase>()
//    private lateinit var repository: MovieRepository
//
//    @Before
//    fun setUp() {
//        Log.e = mock() // You can mock the error output to avoid failures.
//        repository = MovieRepository(service, mock(), detailDao, mock())
//
//    }
//    @Test
//    fun `search should handle empty response`() = runBlocking {
//        // Arrange: Mock the API response
//        val query = "RandomQuery"
//        val page = 1
//        val movieResponse = mock<MovieResponse>()
//
//        // Mock the service to return the mocked MovieResponse
//        `when`(service.searchMovies(query, page)).thenReturn(movieResponse)
//
//        // Act: Call the search method
//        val result = repository.search(query, page)
//
//        // Assert: Verify that the service was called with the correct parameters
//        verify(service).searchMovies(query, page)
//
//        // Assert: Check that the result is the same as the mocked response
//        assertEquals(movieResponse, result)
//    }
//
//    @Test
//    fun `getDetail should fetch movie details from API`() = runBlocking {
//        // Arrange
//        val movieId = 1
//        val movieDetailResponse = mock<MovieDetailEntity>()
//
//        // Mock the service to return the movie details from the API
//        whenever(service.getMovieDetails(movieId)).thenReturn(mock())
//        whenever(service.getMovieDetails(movieId).toEntity()).thenReturn(movieDetailResponse)
//
//        // Act: Call the getDetail method
//        val result = repository.getDetail(movieId)
//
//        // Assert: Verify that the API call was made
//        verify(service).getMovieDetails(movieId)
//        // Assert: Check that the result is the expected entity
//        assert(result == movieDetailResponse)
//    }
//
//    @Test
//    fun `getDetail should fetch movie details from DB if API call fails`() = runBlocking {
//        // Arrange
//        val movieId = 1
//        val movieDetailEntity = mock<MovieDetailEntity>()
//
//        // Mock the service to throw an exception (simulating API failure)
//        whenever(service.getMovieDetails(movieId)).thenThrow(Exception("API Error"))
//
//        // Mock the database to return a movie detail
//        whenever(detailDao.getById(movieId)).thenReturn(movieDetailEntity)
//
//        // Act: Call the getDetail method
//        val result = repository.getDetail(movieId)
//
//        // Assert: Verify that the API call was attempted and failed
//        verify(service).getMovieDetails(movieId)
//        // Assert: Verify that the DB was queried for the movie details
//        verify(detailDao).getById(movieId)
//        // Assert: Check that the result is the expected entity from the DB
//        assert(result == movieDetailEntity)
//    }
//
//
//
//}
