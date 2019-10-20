package co.icanteach.projectx.ui.populartvshows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.icanteach.projectx.common.Resource
import co.icanteach.projectx.common.RxAwareViewModel
import co.icanteach.projectx.common.ui.plusAssign
import co.icanteach.projectx.domain.FetchSearchMoviesUseCase
import co.icanteach.projectx.ui.populartvshows.model.SearchMovieItem
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class PopularTVShowsViewModel @Inject constructor(private val fetchSearchMoviesUseCase: FetchSearchMoviesUseCase) :
    RxAwareViewModel() {

    private val searchMoviesLiveData = MutableLiveData<SearchMovieFeedViewState>()
    val currentQueryStringLiveData = MutableLiveData<String>()

    fun getSearchMoviesLiveData(): LiveData<SearchMovieFeedViewState> = searchMoviesLiveData

    fun fetchMovies(search: String, page: Int) {
        fetchSearchMoviesUseCase
            .fetchMovies(search, page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onMoviesResultReady)
            .also {
                disposable += it
            }
    }

    private fun onMoviesResultReady(resource: Resource<List<SearchMovieItem>>) {
        searchMoviesLiveData.value = SearchMovieFeedViewState(
            status = resource.status,
            error = resource.error,
            data = resource.data
        )
    }
}