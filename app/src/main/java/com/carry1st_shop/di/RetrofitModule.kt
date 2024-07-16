package com.carry1st_shop.di

import com.carry1st_shop.apiInterface.ProductsApiService
import com.carry1st_shop.utils.Urls
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private fun provideRetrofit(): Retrofit {
        //okhttp interceptor is used to log retrofit responses especially when debugging.
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return Retrofit.Builder()
            .baseUrl(Urls.HOST_URL)
            .addConverterFactory(GsonConverterFactory.create())

            .client(
                OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(70, TimeUnit.SECONDS)
                    .writeTimeout(70, TimeUnit.SECONDS)
                    .readTimeout(70, TimeUnit.SECONDS).build()
            )

            .build()
    }
    @Singleton
    @Provides
    fun provideProductApi(): ProductsApiService {
        return provideRetrofit().create(ProductsApiService::class.java)
    }
}