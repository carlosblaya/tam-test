package com.carlosblaya.theagilemonkeystest.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.room.Room
import com.carlosblaya.theagilemonkeystest.TAMApplication
import com.carlosblaya.theagilemonkeystest.data.database.AppDatabase
import com.carlosblaya.theagilemonkeystest.data.response.mapper.AlbumListMapper
import com.carlosblaya.theagilemonkeystest.data.response.mapper.ArtistListMapper
import com.carlosblaya.theagilemonkeystest.data.network.ResponseHandler
import com.carlosblaya.theagilemonkeystest.data.network.services.AlbumsApiInterface
import com.carlosblaya.theagilemonkeystest.data.network.services.ArtistsApiInterface
import com.carlosblaya.theagilemonkeystest.data.network.services.SongsApiInterface
import com.carlosblaya.theagilemonkeystest.domain.repository.AlbumListRepository
import com.carlosblaya.theagilemonkeystest.domain.repository.ArtistListRepository
import com.carlosblaya.theagilemonkeystest.domain.repository.ArtistRepositoryImpl
import com.carlosblaya.theagilemonkeystest.domain.repository.SongListRepository
import com.carlosblaya.theagilemonkeystest.ui.main.MainViewModel
import com.carlosblaya.theagilemonkeystest.ui.search.albums.AlbumsViewModel
import com.carlosblaya.theagilemonkeystest.ui.search.artists.SearchArtistsViewModel
import com.carlosblaya.theagilemonkeystest.ui.search.songs.SongsViewModel
import com.carlosblaya.theagilemonkeystest.util.Konsts
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val repositoryModule: Module = module {
    fun provideResponseHandler(): ResponseHandler {
        return ResponseHandler()
    }
    single<ArtistListRepository> { ArtistRepositoryImpl(get(), get()) }
    single {
        AlbumListRepository(get(), provideResponseHandler())
    }
    single {
        SongListRepository(get(), provideResponseHandler())
    }

}

val mappersModule = module {
    single { ArtistListMapper() }
    single { AlbumListMapper() }
}

val serviceModule: Module = module {
    fun provideArtistsUseApi(retrofit: Retrofit): ArtistsApiInterface {
        return retrofit.create(ArtistsApiInterface::class.java)
    }
    fun provideAlbumsUseApi(retrofit: Retrofit): AlbumsApiInterface {
        return retrofit.create(AlbumsApiInterface::class.java)
    }
    fun provideSongsUseApi(retrofit: Retrofit): SongsApiInterface {
        return retrofit.create(SongsApiInterface::class.java)
    }
    single { provideArtistsUseApi(get()) }
    single { provideAlbumsUseApi(get()) }
    single { provideSongsUseApi(get()) }
}

val retrofitModule = module {

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    fun provideHttpClient(): OkHttpClient {
        //Caching Retrofit responses
        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(TAMApplication.context.cacheDir, cacheSize)
        val okHttpClientBuilder = OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(TAMApplication.context)!!)
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                else
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                chain.proceed(request)
            }
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Konsts.BASE_URL_SERVICE)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()
    }

    single { provideGson() }
    single { provideHttpClient() }
    single { provideRetrofit(get(), get()) }
}

val dbModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, Konsts.DATABASE_NAME).allowMainThreadQueries()
            .build()
    }
}

val viewModelModule: Module = module {
    viewModel { MainViewModel(get()) }
    viewModel { SearchArtistsViewModel(get()) }
    viewModel { AlbumsViewModel(get()) }
    viewModel { SongsViewModel(get(),get()) }

}