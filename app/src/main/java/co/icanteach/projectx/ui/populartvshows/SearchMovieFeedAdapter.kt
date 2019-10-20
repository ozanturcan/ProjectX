package co.icanteach.projectx.ui.populartvshows

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.icanteach.projectx.R
import co.icanteach.projectx.common.ui.inflate
import co.icanteach.projectx.databinding.ItemPopularTvShowsFeedBinding
import co.icanteach.projectx.ui.populartvshows.model.SearchMovieItem
import javax.inject.Inject

class SearchMovieFeedAdapter @Inject constructor() :
    RecyclerView.Adapter<SearchMovieFeedAdapter.SearchMoviesFeedItemViewHolder>() {

    private var searchMovies: MutableList<SearchMovieItem> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchMoviesFeedItemViewHolder {
        val itemBinding = parent.inflate<ItemPopularTvShowsFeedBinding>(R.layout.item_popular_tv_shows_feed, false)
        return SearchMoviesFeedItemViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = searchMovies.size

    override fun onBindViewHolder(holder: SearchMoviesFeedItemViewHolder, position: Int) {
        holder.bind(getTvShow(position))
    }

    private fun getTvShow(position: Int) = searchMovies[position]

    fun setTvShows(tvShows: List<SearchMovieItem>) {
        val beforeSize = searchMovies.size
        searchMovies.addAll(tvShows)
        notifyItemRangeInserted(beforeSize, tvShows.size)
    }

    fun clearTvShows() {
        searchMovies.clear()
        notifyDataSetChanged()
    }

    inner class SearchMoviesFeedItemViewHolder(private val binding: ItemPopularTvShowsFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: SearchMovieItem) {
            with(binding) {
                viewState = SearchMoviesFeedItemViewState(tvShow)
                executePendingBindings()
            }
        }

    }
}