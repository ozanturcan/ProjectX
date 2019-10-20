package co.icanteach.projectx.data.feed.response

import com.google.gson.annotations.SerializedName

class SearchMovieItemResponse(
    @SerializedName("Poster")
    val imageUrl: String?,
    @SerializedName("Title")
    val title: String?,
    @SerializedName("Year")
    val releaseYear: String?,
    @SerializedName("imdbID")
    val imdbID: String?
)