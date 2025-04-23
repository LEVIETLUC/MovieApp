//package com.example.movieapp.db
//
//import android.content.Context
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.paging.PagingSource
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.google.common.truth.Truth.assertThat
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runTest
//import org.junit.*
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class MovieDaoTest {
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var database: TmdbDatabase
//    private lateinit var movieDao: MovieDao
//
//    @Before
//    fun setup() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        database = Room.inMemoryDatabaseBuilder(context, TmdbDatabase::class.java)
//            .allowMainThreadQueries()
//            .build()
//        movieDao = database.movieDao()
//    }
//
//    @After
//    fun teardown() {
//        database.close()
//    }
//
//    @Test
//    fun insertAll_and_getAllMovies() = runTest {
//        val movies = listOf(
//            MovieEntity(id = 1, title = "Movie 1", page = 1),
//            MovieEntity(id = 2, title = "Movie 2", page = 1)
//        )
//        movieDao.insertAll(movies)
//
//        val allMovies = movieDao.getAllMovies()
//        assertThat(allMovies).hasSize(2)
//        assertThat(allMovies).containsExactlyElementsIn(movies)
//    }
//
//    @Test
//    fun clearAll_and_verifyEmpty() = runTest {
//        val movies = listOf(
//            MovieEntity(id = 1, title = "Movie 1", page = 1),
//            MovieEntity(id = 2, title = "Movie 2", page = 1)
//        )
//        movieDao.insertAll(movies)
//
//        val deletedCount = movieDao.clearAll()
//        assertThat(deletedCount).isEqualTo(2)
//
//        val allMovies = movieDao.getAllMovies()
//        assertThat(allMovies).isEmpty()
//    }
//
//    @Test
//    fun pagingSource_returnsCorrectData() = runTest {
//        val movies = listOf(
//            MovieEntity(id = 1, title = "Movie 1", page = 1),
//            MovieEntity(id = 2, title = "Movie 2", page = 1),
//            MovieEntity(id = 3, title = "Movie 3", page = 2)
//        )
//        movieDao.insertAll(movies)
//
//        val pagingSource = movieDao.pagingSource(1)
//        val loadResult = pagingSource.load(
//            PagingSource.LoadParams.Refresh(
//                key = null,
//                loadSize = 2,
//                placeholdersEnabled = false
//            )
//        )
//        assertThat((loadResult as PagingSource.LoadResult.Page).data).containsExactly(
//            MovieEntity(id = 1, title = "Movie 1", page = 1),
//            MovieEntity(id = 2, title = "Movie 2", page = 1)
//        )
//    }
//}
