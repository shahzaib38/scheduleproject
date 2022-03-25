package sb.app.messageschedular.api

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import sb.app.messageschedular.model.UnSplashImage

interface UnSplashApi {



    companion object{
        const val API_KEY ="3JWChLn2m94fGrhpyqO4cC9UwKmBvkb1VqP0xc3lFQs" }

    @Headers("Authorization: Client-ID ${API_KEY}")
    @GET("/photos")
   suspend  fun getAllImages(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<UnSplashImage>
}