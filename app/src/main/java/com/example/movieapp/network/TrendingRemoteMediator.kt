package com.example.movieapp.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.RemoteMediator
import androidx.paging.RemoteMediator.MediatorResult
import androidx.room.withTransaction
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.db.TmdbDatabase
import com.example.movieapp.network.TmdbService

@OptIn(ExperimentalPagingApi::class)
class TrendingRemoteMediator(
    private val service: TmdbService,
    private val db: TmdbDatabase
) : RemoteMediator<Int, MovieEntity>() {

    private val movieDao = db.movieDao()

    override suspend fun load(
        loadType: LoadType,
        state: androidx.paging.PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                if (lastItem == null) 1 else lastItem.page + 1
            }
        }

        return try {
            val response = service.getTrending(page)
            val movies = response.results.map { it.toEntity(page) }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearAll()     // bạn cần thêm hàm này trong dao
                }
                movieDao.insertAll(movies)
            }

            MediatorResult.Success(endOfPaginationReached = response.results.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
