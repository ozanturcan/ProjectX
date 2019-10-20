package co.icanteach.projectx.ui.populartvshows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.icanteach.projectx.common.Resource
import co.icanteach.projectx.common.RxAwareViewModel
import co.icanteach.projectx.common.ui.plusAssign
import co.icanteach.projectx.domain.FetchPopularTvShowUseCase
import co.icanteach.projectx.ui.populartvshows.model.SearchMovieItem
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class PopularTVShowsViewModel @Inject constructor(private val fetchPopularTvShowUseCase: FetchPopularTvShowUseCase) :
    RxAwareViewModel() {

    private val popularTvShowsLiveData = MutableLiveData<SearchMovieFeedViewState>()

    fun getPopularTvShowsLiveData(): LiveData<SearchMovieFeedViewState> = popularTvShowsLiveData

    fun fetchMovies(search: String, page: Int) {
        fetchPopularTvShowUseCase
            .fetchMovies(search, page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onMoviesResultReady)
            .also {
                disposable += it
            }
    }

    private fun onMoviesResultReady(resource: Resource<List<SearchMovieItem>>) {
        popularTvShowsLiveData.value = SearchMovieFeedViewState(
            status = resource.status,
            error = resource.error,
            data = resource.data
        )
    }
}