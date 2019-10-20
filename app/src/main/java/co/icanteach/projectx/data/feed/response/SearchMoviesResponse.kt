package co.icanteach.projectx.data.feed.response

import com.google.gson.annotations.SerializedName

class SearchMoviesResponse(
    @SerializedName("Search")
    val search: List<SearchMovieItemResponse>
)