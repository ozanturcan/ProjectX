package co.icanteach.projectx

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    SearchMoviesViewModelTest::class,
    SearchMoviesFeedItemViewStateTest::class,
    SearchMoviesFeedViewStateTest::class
)
class SearchMoviesTestSuite