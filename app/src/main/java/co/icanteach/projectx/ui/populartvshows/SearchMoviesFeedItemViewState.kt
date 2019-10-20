package co.icanteach.projectx.ui.populartvshows

import co.icanteach.projectx.ui.populartvshows.model.SearchMovieItem

class SearchMoviesFeedItemViewState(private val tvShow: SearchMovieItem) {

    fun getImageUrl() = tvShow.imageUrl

    fun getMovieName() = tvShow.title
    fun getMovieOverview() = tvShow.releaseYear
    fun getMovieImdbID() = tvShow.imdbID
}