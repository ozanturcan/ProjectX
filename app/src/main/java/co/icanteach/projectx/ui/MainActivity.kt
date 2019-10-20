package co.icanteach.projectx.ui

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import co.icanteach.projectx.R
import co.icanteach.projectx.common.ui.EndlessScrollListener
import co.icanteach.projectx.common.ui.observeNonNull
import co.icanteach.projectx.common.ui.withOutEmptyChar
import co.icanteach.projectx.databinding.ActivityMainBinding
import co.icanteach.projectx.ui.searchMovies.SearchMovieFeedAdapter
import co.icanteach.projectx.ui.searchMovies.SearchMovieFeedViewState
import co.icanteach.projectx.ui.searchMovies.SearchMoviesViewModel
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import dagger.android.AndroidInjection
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var moviesFeedAdapter: SearchMovieFeedAdapter

    private lateinit var moviesViewModel: SearchMoviesViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        moviesViewModel =
            ViewModelProviders.of(this, viewModelProviderFactory)
                .get(SearchMoviesViewModel::class.java)

        moviesViewModel.getSearchMoviesLiveData().observeNonNull(this) {
            renderSearchMovies(it)
        }

        initSearchMoviesRecyclerView()
        initMaterialDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val mSearch = menu.findItem(R.id.action_search)

        val mSearchView = mSearch.actionView as SearchView
        mSearchView.queryHint = resources.getString(R.string.menu_search)

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.length > 2) {
                    moviesFeedAdapter.clearMovies()
                    moviesViewModel.currentQueryStringLiveData.value = query
                    fetchMovies(query, FIRST_PAGE)

                } else Toast.makeText(
                    this@MainActivity,
                    "Min searchable text length is 3",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun initSearchMoviesRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.apply {
            adapter = moviesFeedAdapter
            layoutManager = linearLayoutManager
            addOnScrollListener(object : EndlessScrollListener(linearLayoutManager) {
                override fun onLoadMore(page: Int) {
                    if (moviesViewModel.currentQueryStringLiveData.value.toString().length > 2)
                        fetchMovies(
                            moviesViewModel.currentQueryStringLiveData.value.toString(),
                            page
                        )
                }
            })
        }
    }

    private fun renderSearchMovies(feedViewState: SearchMovieFeedViewState) {
        with(binding) {
            viewState = feedViewState
            executePendingBindings()
        }
        moviesFeedAdapter.setMovies(feedViewState.getSearchMovies())
    }

    private fun fetchMovies(search: String, page: Int) {
        moviesViewModel.fetchMovies(search, page)
    }

    companion object {
        const val FIRST_PAGE = 1
    }


    private fun initMaterialDialog() {
        val dialog = MaterialDialog(this, BottomSheet(LayoutMode.MATCH_PARENT)).show {
            customView(
                R.layout.bottom_sheet_consecutive_repeating_view,
                scrollable = true,
                horizontalPadding = true
            )
            onCancel { }
            noAutoDismiss()
            setPeekHeight(literal = 200)
            cornerRadius(16f)
        }
        val customView = dialog.getCustomView()
        val etRepeatingString: AppCompatEditText = customView.findViewById(R.id.etRepeatingString)
        val etInput: AppCompatEditText = customView.findViewById(R.id.etInput)
        val tvOutput: AppCompatTextView = customView.findViewById(R.id.tvOutput)
        val btnCompare: AppCompatButton = customView.findViewById(R.id.btnCompare)

        btnCompare.setOnClickListener {
            val hashMap =
                calculateConsecutiveCharacterList(etRepeatingString.text.toString().withOutEmptyChar())
            var result = etRepeatingString.text.toString()
            val number = etInput.text.toString().toInt()
            for (i in result.length downTo number) {
                hashMap[i]?.forEach { string ->
                    result = result.replace(string, string.replace(Regex("(?s)."), "*"))
                }
            }
            tvOutput.text = result
            println("println result :$result")
        }

    }

    private fun calculateConsecutiveCharacterList(repeatingString: String): HashMap<Int, HashSet<String>> {
        val repeatingMap: HashMap<Int, HashSet<String>> = hashMapOf()

        val length = repeatingString.length
        // Find the maximum repeating character
        // starting from repeatingString[i]
        var count = 0
        while (count < length) {
            var currentCount = 1
            val stringBuilder = StringBuilder().append(repeatingString[count])
            for (j in count + 1 until length) {
                if (repeatingString[count] != repeatingString[j])
                    break
                currentCount++
                stringBuilder.append(repeatingString[count])
            }
            if (repeatingMap.containsKey(currentCount)) {
                count += currentCount
                repeatingMap[currentCount]!!.add(stringBuilder.toString())
            } else {
                count += currentCount
                repeatingMap[currentCount] =
                    hashSetOf<String>().apply { add(stringBuilder.toString()) }
            }
        }

        println("println repeatingMap :" + repeatingMap.toList())
        return repeatingMap
    }

}