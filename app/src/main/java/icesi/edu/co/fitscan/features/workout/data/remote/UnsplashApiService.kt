package icesi.edu.co.fitscan.features.workout.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UnsplashApiService {
    
    @GET("search/photos")
    suspend fun searchPhotos(
        @Header("Authorization") authorization: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("orientation") orientation: String = "landscape"
    ): Response<UnsplashSearchResponse>
}

data class UnsplashSearchResponse(
    val total: Int,
    val total_pages: Int,
    val results: List<UnsplashPhoto>
)

data class UnsplashPhoto(
    val id: String,
    val created_at: String,
    val width: Int,
    val height: Int,
    val color: String?,
    val blur_hash: String?,
    val likes: Int,
    val liked_by_user: Boolean,
    val description: String?,
    val user: UnsplashUser,
    val urls: UnsplashUrls,
    val links: UnsplashLinks
)

data class UnsplashUser(
    val id: String,
    val username: String,
    val name: String,
    val first_name: String?,
    val last_name: String?,
    val instagram_username: String?,
    val twitter_username: String?,
    val portfolio_url: String?,
    val profile_image: UnsplashProfileImage?
)

data class UnsplashProfileImage(
    val small: String,
    val medium: String,
    val large: String
)

data class UnsplashUrls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)

data class UnsplashLinks(
    val self: String,
    val html: String,
    val download: String
)
