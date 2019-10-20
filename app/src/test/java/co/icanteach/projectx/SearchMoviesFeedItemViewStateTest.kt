package co.icanteach.projectx

import co.icanteach.projectx.ui.searchMovies.SearchMoviesFeedItemViewState
import co.icanteach.projectx.ui.searchMovies.model.SearchMovieItem
import com.google.common.truth.Truth
import org.junit.Test

class SearchMoviesFeedItemViewStateTest {

    @Test
    fun `should match movie name for given movie`() {

        // Given
        val movie = createDummyMovie()
        val givenViewState = SearchMoviesFeedItemViewState(movie)

        // When
        val actualResult = givenViewState.getMovieName()

        // Then
        Truth.assertThat(actualResult).isEqualTo("Love Actually")
    }

    @Test
    fun `should match image url for given movie`() {

        // Given
        val movie = createDummyMovie()
        val givenViewState = SearchMoviesFeedItemViewState(movie)

        // When
        val actualResult = givenViewState.getImageUrl()

        // Then
        Truth.assertThat(actualResult)
            .isEqualTo("https://m.media-amazon.com/images/M/MV5BZWI3ZTMxNjctMjdlNS00NmUwLWFiM2YtZDUyY2I3N2MxYTE0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg")
    }

    @Test
    fun `should match overview for given movie`() {

        // Given
        val movie = createDummyMovie()
        val givenViewState = SearchMoviesFeedItemViewState(movie)

        // When
        val actualResult = givenViewState.getMovieReleaseYear()

        // Then
        Truth.assertThat(actualResult).isEqualTo("2003")
    }

    private fun createDummyMovie(): SearchMovieItem {
        return SearchMovieItem(
            title = "Love Actually",
            imageUrl = "https://m.media-amazon.com/images/M/MV5BZWI3ZTMxNjctMjdlNS00NmUwLWFiM2YtZDUyY2I3N2MxYTE0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg",
            releaseYear = "2003",
            imdbID = "tt0314331"
        )
    }
}