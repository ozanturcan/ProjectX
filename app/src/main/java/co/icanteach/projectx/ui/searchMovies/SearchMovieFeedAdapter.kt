package co.icanteach.projectx.ui.searchMovies

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.icanteach.projectx.R
import co.icanteach.projectx.common.ui.inflate
import co.icanteach.projectx.databinding.ItemSearchMoviesFeedBinding
import co.icanteach.projectx.ui.searchMovies.model.SearchMovieItem
import javax.inject.Inject

class SearchMovieFeedAdapter @Inject constructor() :
    RecyclerView.Adapter<SearchMovieFeedAdapter.SearchMoviesFeedItemViewHolder>() {

    private var searchMovies: MutableList<SearchMovieItem> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchMoviesFeedItemViewHolder {
        val itemBinding =
            parent.inflate<ItemSearchMoviesFeedBinding>(R.layout.item_search_movies_feed, false)
        return SearchMoviesFeedItemViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = searchMovies.size

    override fun onBindViewHolder(holder: SearchMoviesFeedItemViewHolder, position: Int) {
        holder.bind(getMovie(position))
    }

    private fun getMovie(position: Int) = searchMovies[position]

    fun setMovies(movies: List<SearchMovieItem>) {
        val beforeSize = searchMovies.size
        searchMovies.addAll(movies)
        notifyItemRangeInserted(beforeSize, movies.size)
    }

    fun clearMovies() {
        searchMovies.clear()
        notifyDataSetChanged()
    }

    inner class SearchMoviesFeedItemViewHolder(private val binding: ItemSearchMoviesFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: SearchMovieItem) {
            with(binding) {
                viewState = SearchMoviesFeedItemViewState(movie)
                executePendingBindings()
            }
        }

    }
}