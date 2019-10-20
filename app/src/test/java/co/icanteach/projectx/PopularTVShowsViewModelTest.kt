package co.icanteach.projectx

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import co.icanteach.RxImmediateSchedulerRule
import co.icanteach.projectx.common.Resource
import co.icanteach.projectx.common.Status
import co.icanteach.projectx.common.ui.applyLoading
import co.icanteach.projectx.domain.FetchPopularTvShowUseCase
import co.icanteach.projectx.ui.populartvshows.PopularTVShowsViewModel
import co.icanteach.projectx.ui.populartvshows.SearchMovieFeedViewState
import co.icanteach.projectx.ui.populartvshows.model.SearchMovieItem
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

class PopularTVShowsViewModelTest {

    @MockK
    lateinit var fetchPopularTvShowUseCase: FetchPopularTvShowUseCase

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var popularTVShowsViewModel: PopularTVShowsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        popularTVShowsViewModel = PopularTVShowsViewModel(fetchPopularTvShowUseCase)
    }

    @Test
    fun `given loading state, when fetchMovies called, then update live data for loading status`() {

        // Given
        val mockedObserver = createPopularTVShowsFeedObserver()
        popularTVShowsViewModel.getPopularTvShowsLiveData()
            .observeForever(mockedObserver)

        every { fetchPopularTvShowUseCase.fetchMovies(any()) } returns
                Observable.just(Resource.success(createPopularTVShows()))
                    .compose(applyLoading())

        // When
        popularTVShowsViewModel.fetchMovies(1)

        // Then
        val popularTVShowsFeedViewStateSlots = mutableListOf<SearchMovieFeedViewState>()
        verify { mockedObserver.onChanged(capture(popularTVShowsFeedViewStateSlots)) }

        val errorState = popularTVShowsFeedViewStateSlots[0]
        Truth.assertThat(errorState.status).isEqualTo(Status.LOADING)

        verify { fetchPopularTvShowUseCase.fetchMovies(any()) }
    }

    @Test
    fun `given success state, when fetchMovies called, then update live data for success status`() {
        // Given
        val mockedObserver = createPopularTVShowsFeedObserver()
        popularTVShowsViewModel.getPopularTvShowsLiveData()
            .observeForever(mockedObserver)

        every { fetchPopularTvShowUseCase.fetchMovies(any()) } returns
                Observable.just(Resource.success(createPopularTVShows()))
                    .compose(applyLoading())

        // When
        popularTVShowsViewModel.fetchMovies(1)

        // Then
        val popularTVShowsFeedViewStateSlots = mutableListOf<SearchMovieFeedViewState>()
        verify(exactly = 2) { mockedObserver.onChanged(capture(popularTVShowsFeedViewStateSlots)) }

        val errorState = popularTVShowsFeedViewStateSlots[1]
        Truth.assertThat(errorState.status).isEqualTo(Status.SUCCESS)

        verify { fetchPopularTvShowUseCase.fetchMovies(any()) }
    }

    @Test
    fun `given error state, when fetchMovies called, then update live data for error status`() {
        // Given
        val mockedObserver = createPopularTVShowsFeedObserver()
        popularTVShowsViewModel.getPopularTvShowsLiveData()
            .observeForever(mockedObserver)

        every { fetchPopularTvShowUseCase.fetchMovies(any()) } returns
                Observable.just(Resource.error<List<SearchMovieItem>>(Exception("unhandled exception")))
                    .compose(applyLoading())

        // When
        popularTVShowsViewModel.fetchMovies(1)

        // Then
        val popularTVShowsFeedViewStateSlots = mutableListOf<SearchMovieFeedViewState>()
        verify(exactly = 2) { mockedObserver.onChanged(capture(popularTVShowsFeedViewStateSlots)) }

        val errorState = popularTVShowsFeedViewStateSlots[1]
        Truth.assertThat(errorState.status).isEqualTo(Status.ERROR)

        verify { fetchPopularTvShowUseCase.fetchMovies(any()) }
    }

    private fun createPopularTVShowsFeedObserver(): Observer<SearchMovieFeedViewState> =
        spyk(Observer { })

    private fun createDummyTvShow(): SearchMovieItem {
        return SearchMovieItem(
            title = "Chernobyl",
            imageUrl = "/hlLXt2tOPT6RRnjiUmoxyG1LTFi.jpg",
            releaseYear = "An unassuming mystery writer turned sleuth uses her professional insight to help solve real-life homicide cases."
        )
    }

    private fun createPopularTVShows(): List<SearchMovieItem> {
        val popularTvShows = mutableListOf<SearchMovieItem>()
        for (x in 0..10) {
            popularTvShows.add(createDummyTvShow())
        }
        return popularTvShows
    }
}