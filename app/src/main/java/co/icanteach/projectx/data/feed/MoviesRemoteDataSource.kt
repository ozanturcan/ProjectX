package co.icanteach.projectx.data.feed

import co.icanteach.projectx.data.InterviewRestInterface
import javax.inject.Inject

class MoviesRemoteDataSource @Inject constructor(private val restInterface: InterviewRestInterface) {

    fun fetchMovies(search: String, page: Int) = restInterface.fetchMovies(search, page)

}