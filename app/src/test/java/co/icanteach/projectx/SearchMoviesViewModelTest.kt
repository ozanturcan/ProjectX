package co.icanteach.projectx

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import co.icanteach.RxImmediateSchedulerRule
import co.icanteach.projectx.common.Resource
import co.icanteach.projectx.common.Status
import co.icanteach.projectx.common.ui.applyLoading
import co.icanteach.projectx.domain.FetchSearchMoviesUseCase
import co.icanteach.projectx.ui.searchMovies.SearchMovieFeedViewState
import co.icanteach.projectx.ui.searchMovies.SearchMoviesViewModel
import co.icanteach.projectx.ui.searchMovies.model.SearchMovieItem
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchMoviesViewModelTest {

    @MockK
    lateinit var fetchSearchMovieUseCase: FetchSearchMoviesUseCase

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var searchMoviesViewModel: SearchMoviesViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        searchMoviesViewModel = SearchMoviesViewModel(fetchSearchMovieUseCase)
    }

    @Test
    fun `given loading state, when fetchMovies called, then update live data for loading status`() {

        // Given
        val mockedObserver = createSearchMoviesFeedObserver()
        searchMoviesViewModel.getSearchMoviesLiveData()
            .observeForever(mockedObserver)

        every { fetchSearchMovieUseCase.fetchMovies("love", any()) } returns
                Observable.just(Resource.success(createSearchMovies()))
                    .compose(applyLoading())

        // When
        searchMoviesViewModel.fetchMovies("love", 1)

        // Then
        val searchMoviesFeedViewStateSlots = mutableListOf<SearchMovieFeedViewState>()
        verify { mockedObserver.onChanged(capture(searchMoviesFeedViewStateSlots)) }

        val errorState = searchMoviesFeedViewStateSlots[0]
        Truth.assertThat(errorState.status).isEqualTo(Status.LOADING)

        verify { fetchSearchMovieUseCase.fetchMovies("love", any()) }
    }

    @Test
    fun `given success state, when fetchMovies called, then update live data for success status`() {
        // Given
        val mockedObserver = createSearchMoviesFeedObserver()
        searchMoviesViewModel.getSearchMoviesLiveData()
            .observeForever(mockedObserver)

        every { fetchSearchMovieUseCase.fetchMovies("love", any()) } returns
                Observable.just(Resource.success(createSearchMovies()))
                    .compose(applyLoading())

        // When
        searchMoviesViewModel.fetchMovies("love", 1)

        // Then
        val searchMoviesFeedViewStateSlots = mutableListOf<SearchMovieFeedViewState>()
        verify(exactly = 2) { mockedObserver.onChanged(capture(searchMoviesFeedViewStateSlots)) }

        val errorState = searchMoviesFeedViewStateSlots[1]
        Truth.assertThat(errorState.status).isEqualTo(Status.SUCCESS)

        verify { fetchSearchMovieUseCase.fetchMovies("love", any()) }
    }

    @Test
    fun `given error state, when fetchMovies called, then update live data for error status`() {
        // Given
        val mockedObserver = createSearchMoviesFeedObserver()
        searchMoviesViewModel.getSearchMoviesLiveData()
            .observeForever(mockedObserver)

        every { fetchSearchMovieUseCase.fetchMovies("love", any()) } returns
                Observable.just(Resource.error<List<SearchMovieItem>>(Exception("unhandled exception")))
                    .compose(applyLoading())

        // When
        searchMoviesViewModel.fetchMovies("love", 1)

        // Then
        val searchMoviesFeedViewStateSlots = mutableListOf<SearchMovieFeedViewState>()
        verify(exactly = 2) { mockedObserver.onChanged(capture(searchMoviesFeedViewStateSlots)) }

        val errorState = searchMoviesFeedViewStateSlots[1]
        Truth.assertThat(errorState.status).isEqualTo(Status.ERROR)

        verify { fetchSearchMovieUseCase.fetchMovies("love", any()) }
    }

    private fun createSearchMoviesFeedObserver(): Observer<SearchMovieFeedViewState> =
        spyk(Observer { })

    private fun createDummyMovie(): SearchMovieItem {
        return SearchMovieItem(
            title = "Love Actually",
            imageUrl = "https://m.media-amazon.com/images/M/MV5BZWI3ZTMxNjctMjdlNS00NmUwLWFiM2YtZDUyY2I3N2MxYTE0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg",
            releaseYear = "2003",
            imdbID = "tt0314331"
        )
    }

    private fun createSearchMovies(): List<SearchMovieItem> {
        val searchMovies = mutableListOf<SearchMovieItem>()
        for (x in 0..10) {
            searchMovies.add(createDummyMovie())
        }
        return searchMovies
    }
}