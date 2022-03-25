package sb.app.messageschedular.di

import android.app.Application
import android.content.Context
//import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
//import kotlinx.serialization.ExperimentalSerializationApi
//import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sb.app.messageschedular.api.UnSplashApi
import sb.app.messageschedular.contactHelper.ContactHelper
import sb.app.messageschedular.database.SmsDatabase
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {




    @Singleton
    @Provides
    fun provideTransferRetrofit(@ApplicationContext applicationContext: Context) : SmsDatabase {
        return SmsDatabase.getInstance(applicationContext) }




    @Singleton
    fun getContactHelper(@ApplicationContext applicationContext: Context):ContactHelper{
        return ContactHelper(applicationContext) }


    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {
      return   OkHttpClient.Builder()
          .readTimeout(10,TimeUnit.SECONDS)
          .connectTimeout(10,TimeUnit.SECONDS).build() }

 //   @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideRetrofitImage(okHttpClient: OkHttpClient):Retrofit{

//        val contentType = MediaType.get("application/json")
//        val json = Json {
//            ignoreUnknownKeys = true
//        }

        return Retrofit.Builder()
            .baseUrl("https://api.unsplash.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build() }

    @Singleton
    @Provides
    fun getUnsplashApi(retrofit: Retrofit):UnSplashApi{

        return retrofit.create(UnSplashApi::class.java)
    }





}