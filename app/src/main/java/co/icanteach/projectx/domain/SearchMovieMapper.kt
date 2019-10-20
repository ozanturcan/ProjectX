package co.icanteach.projectx.domain

import co.icanteach.projectx.common.Mapper
import co.icanteach.projectx.data.feed.response.SearchMoviesResponse
import co.icanteach.projectx.ui.populartvshows.model.SearchMovieItem
import javax.inject.Inject

class SearchMovieMapper @Inject constructor() :
    Mapper<SearchMoviesResponse, List<SearchMovieItem>> {

    override fun mapFrom(type: SearchMoviesResponse): List<SearchMovieItem> {
        return type.search.map { itemResponse ->
            SearchMovieItem(
                imageUrl = itemResponse.imageUrl,
                title = itemResponse.title,
                releaseYear = itemResponse.releaseYear,
                imdbID = itemResponse.imdbID
            )
        }
    }
}