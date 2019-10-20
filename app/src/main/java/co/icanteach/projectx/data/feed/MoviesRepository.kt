package co.icanteach.projectx.data.feed

import co.icanteach.projectx.common.Resource
import co.icanteach.projectx.common.ui.applyLoading
import co.icanteach.projectx.data.feed.response.SearchMoviesResponse
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MoviesRepository @Inject constructor(private val moviesRemoteDataSource: MoviesRemoteDataSource) {

    fun fetchMovies(search: String, page: Int): Observable<Resource<SearchMoviesResponse>> =
        moviesRemoteDataSource
            .fetchMovies(search, page)
            .map {
                if (it.response) {
                    Resource.success(it)
                } else {
                    Resource.error(Throwable(it.error))
                }
            }
            .onErrorReturn { Resource.error(it) }
            .subscribeOn(Schedulers.io())
            .compose(applyLoading())
}