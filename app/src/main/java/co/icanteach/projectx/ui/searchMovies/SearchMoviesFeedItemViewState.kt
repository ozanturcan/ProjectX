package co.icanteach.projectx.ui.searchMovies

import co.icanteach.projectx.ui.searchMovies.model.SearchMovieItem

class SearchMoviesFeedItemViewState(private val movie: SearchMovieItem) {

    fun getImageUrl() = movie.imageUrl

    fun getMovieName() = movie.title
    fun getMovieReleaseYear() = movie.releaseYear
    fun getMovieImdbID() = movie.imdbID
}