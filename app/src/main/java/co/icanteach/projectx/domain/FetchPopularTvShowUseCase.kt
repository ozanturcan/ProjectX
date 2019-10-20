package co.icanteach.projectx.domain

import co.icanteach.projectx.common.Resource
import co.icanteach.projectx.data.feed.MoviesRepository
import co.icanteach.projectx.ui.populartvshows.model.SearchMovieItem
import io.reactivex.Observable
import javax.inject.Inject

class FetchPopularTvShowUseCase @Inject constructor(
    private val repository: MoviesRepository,
    private val mapper: SearchMovieMapper
) {

    fun fetchMovies(search: String, page: Int): Observable<Resource<List<SearchMovieItem>>> {
        return repository
            .fetchMovies(search, page)
            .map { resource ->
                Resource(
                    status = resource.status,
                    data = resource.data?.let { mapper.mapFrom(it) },
                    error = resource.error
                )
            }
    }
}