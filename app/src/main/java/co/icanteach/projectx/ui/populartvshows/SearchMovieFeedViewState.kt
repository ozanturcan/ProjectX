package co.icanteach.projectx.ui.populartvshows

import co.icanteach.projectx.common.Status
import co.icanteach.projectx.ui.populartvshows.model.SearchMovieItem

class SearchMovieFeedViewState(
    val status: Status,
    val error: Throwable? = null,
    val data: List<SearchMovieItem>? = null
) {
    fun getPopularTvShows() = data ?: mutableListOf()

    fun isLoading() = status == Status.LOADING

    fun getErrorMessage() = error?.message

    fun shouldShowErrorMessage() = error != null
}