package co.icanteach.projectx

import co.icanteach.projectx.common.Status
import co.icanteach.projectx.ui.populartvshows.SearchMovieFeedViewState
import com.google.common.truth.Truth
import org.junit.Test

class PopularTVShowsFeedViewStateTest {

    @Test
    fun `should return loading true when status is loading`() {

        // Given
        val givenViewState =
            SearchMovieFeedViewState(status = Status.LOADING)

        // When
        val actualResult = givenViewState.isLoading()

        // Then
        Truth.assertThat(actualResult).isTrue()
    }

    @Test
    fun `should not return loading false when status is error`() {

        // Given
        val givenViewState =
            SearchMovieFeedViewState(status = Status.SUCCESS)

        // When
        val actualResult = givenViewState.isLoading()

        // Then
        Truth.assertThat(actualResult).isFalse()
    }

    @Test
    fun `should not return loading false when status is success`() {

        // Given
        val givenViewState = SearchMovieFeedViewState(status = Status.ERROR)

        // When
        val actualResult = givenViewState.isLoading()

        // Then
        Truth.assertThat(actualResult).isFalse()
    }

    @Test
    fun `should return correct error message when status is error`() {

        // Given
        val givenViewState =
            SearchMovieFeedViewState(
                status = Status.ERROR,
                error = Exception("500 Internal Server Error")
            )

        // When
        val actualResult = givenViewState.getErrorMessage()

        // Then
        Truth.assertThat(actualResult).isEqualTo("500 Internal Server Error")
    }

    @Test
    fun `should return true for error placeholder visibility  when status is error`() {

        // Given
        val givenViewState =
            SearchMovieFeedViewState(
                status = Status.ERROR,
                error = Exception("500 Internal Server Error")
            )

        // When
        val actualResult = givenViewState.shouldShowErrorMessage()

        // Then
        Truth.assertThat(actualResult).isTrue()
    }
}