package com.example.movieapp.repository


import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.example.movieapp.db.MovieDao
import com.example.movieapp.db.MovieDetailDao
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.db.MovieDetailEntity
import com.example.movieapp.db.TmdbDatabase
import com.example.movieapp.network.TmdbService
import com.example.movieapp.network.TrendingRemoteMediator
import com.example.movieapp.network.model.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieRepository(
    private val service: TmdbService,
    private val movieDao: MovieDao,
    private val detailDao: MovieDetailDao,
    private val db: TmdbDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getTrendingStream(): Flow<PagingData<MovieEntity>> {
        //load data string from dv
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = TrendingRemoteMediator(service, db),
            pagingSourceFactory = { movieDao.pagingSource(page = 1) },
        ).flow.map { pagingData ->
            Log.d("MovieRepository", "getTrendingStream2: ${pagingData.map { it.title }}")
            pagingData
        }

    }


//    fun getTrendingStream(): Flow<PagingData<MovieEntity>> {
//        return Pager(
//            config = PagingConfig(pageSize = 20),
//            Log.e("MovieRepository", "getTrendingStream: ${movieDao.pagingSource(page = 1)}"),
//            pagingSourceFactory = { movieDao.pagingSource(page = 1) }
//        ).flow.map { pagingData ->
//            Log.d("MovieRepository", "getTrendingStream2: ${pagingData.map { it.title }}")
//            pagingData
//        }
//    }

    // Load & cache trending pages
    suspend fun loadAndCacheTrending(page: Int): List<MovieEntity> {
        val resp = service.getTrending(page)
        Log.d("MovieRepository", "loadAndCacheTrending: Fetched ${resp.results.size} movies for page $page")
        Log.d("MovieRepository", "loadAndCacheTrending: raw response = $resp")
        val entities = resp.results.map { it.toEntity(page) }
        withContext(Dispatchers.IO) {
            movieDao.insertAll(entities)
        }
        return entities
    }

    suspend fun search(query: String, page: Int): MovieResponse {
        return service.searchMovies(query, page)
    }

    suspend fun resetDatabase(db: TmdbDatabase) {
        withContext(Dispatchers.IO) {
            db.clearAllTables()
        }
    }

    suspend fun getDetail(id: Int): MovieDetailEntity {
//        detailDao.getById(id)?.let { return it }
        val resp = service.getMovieDetails(id)
        Log.d("MovieRepository", "getDetail: Fetched movie details for id $id")
        Log.d("MovieRepository", "getDetail: raw response = $resp")
        val entity = resp.toEntity()
        resetDatabase(db)
        insertMovieDetail(entity)
        return entity
    }

    suspend fun insertMovieDetail(movieDetail: MovieDetailEntity) {
        return withContext(Dispatchers.IO) {
            detailDao.insert(movieDetail)
        }
    }

    suspend fun getAllMoviesFromDb(): List<MovieEntity> {
        return withContext(Dispatchers.IO) {
            movieDao.getAllMovies()
        }
    }

suspend fun getMoviePagingPage(page: Int): List<MovieEntity> {
    return withContext(Dispatchers.IO) {
        val result = movieDao.pagingSource(page).load(
            PagingSource.LoadParams.Refresh(
                key = page,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        if (result is PagingSource.LoadResult.Page) {
            result.data
        } else {
            emptyList()
        }
    }
}

}