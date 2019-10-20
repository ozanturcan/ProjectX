package co.icanteach.projectx

import co.icanteach.projectx.ui.populartvshows.SearchMoviesFeedItemViewState
import co.icanteach.projectx.ui.populartvshows.model.SearchMovieItem
import com.google.common.truth.Truth
import org.junit.Test

class PopularTVShowsFeedItemViewStateTest {

    @Test
    fun `should match tv show name for given tv show`() {

        // Given
        val tvShow = createDummyTvShow()
        val givenViewState = SearchMoviesFeedItemViewState(tvShow)

        // When
        val actualResult = givenViewState.getMovieName()

        // Then
        Truth.assertThat(actualResult).isEqualTo("Chernobyl")
    }

    @Test
    fun `should match image url for given tv show`() {

        // Given
        val tvShow = createDummyTvShow()
        val givenViewState = SearchMoviesFeedItemViewState(tvShow)

        // When
        val actualResult = givenViewState.getImageUrl()

        // Then
        Truth.assertThat(actualResult).isEqualTo("/hlLXt2tOPT6RRnjiUmoxyG1LTFi.jpg")
    }

    @Test
    fun `should match overview for given tv show`() {

        // Given
        val tvShow = createDummyTvShow()
        val givenViewState = SearchMoviesFeedItemViewState(tvShow)

        // When
        val actualResult = givenViewState.getMovieOverview()

        // Then
        Truth.assertThat(actualResult).isEqualTo("An unassuming mystery writer turned sleuth uses her professional insight to help solve real-life homicide cases.")
    }

    private fun createDummyTvShow(): SearchMovieItem {
        return SearchMovieItem(
            title = "Chernobyl",
            imageUrl = "/hlLXt2tOPT6RRnjiUmoxyG1LTFi.jpg",
            releaseYear = "An unassuming mystery writer turned sleuth uses her professional insight to help solve real-life homicide cases.",
            imdbID = "2019"
        )
    }
}