package co.icanteach.projectx.data

import co.icanteach.projectx.data.feed.response.SearchMoviesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface InterviewRestInterface {

    @GET(".")
    fun fetchMovies(@Query("s") search: String, @Query("page") page: Int, @Query("type") type: String = "movie"): Observable<SearchMoviesResponse>
}