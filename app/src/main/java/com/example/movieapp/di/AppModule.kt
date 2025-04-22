package com.example.movieapp.di

import androidx.room.Room
import com.example.movieapp.db.TmdbDatabase
import com.example.movieapp.network.AuthInterceptor
import com.example.movieapp.network.TmdbService
import com.example.movieapp.repository.MovieRepository
import com.example.movieapp.util.Constants.TMDB_BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(AuthInterceptor())
            .build()
    }

    single {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory()) // Add this line
            .build()

        Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TmdbService::class.java)
    }


    single {
        Room.databaseBuilder(get(), TmdbDatabase::class.java, "tmdb.db")
            .fallbackToDestructiveMigration()  // Sẽ xóa và tạo lại database khi version thay đổi
            .build()
    }

    single { get<TmdbDatabase>().movieDao() }
    single { get<TmdbDatabase>().movieDetailDao() }

    single { MovieRepository(get(), get(), get(), get()) }

    // ViewModels
    viewModel { com.example.movieapp.ui.viewmodels.TrendingViewModel(get()) }
    viewModel { com.example.movieapp.ui.viewmodels.SearchViewModel(get()) }
    viewModel { com.example.movieapp.ui.viewmodels.DetailViewModel(get()) }
}