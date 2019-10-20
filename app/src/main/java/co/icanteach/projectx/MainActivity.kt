package co.icanteach.projectx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import co.icanteach.projectx.common.ui.EndlessScrollListener
import co.icanteach.projectx.common.ui.observeNonNull
import co.icanteach.projectx.common.ui.runIfNull
import co.icanteach.projectx.common.ui.withOutEmptyChar
import co.icanteach.projectx.databinding.ActivityMainBinding
import co.icanteach.projectx.ui.populartvshows.PopularTVShowsFeedAdapter
import co.icanteach.projectx.ui.populartvshows.PopularTVShowsFeedViewState
import co.icanteach.projectx.ui.populartvshows.PopularTVShowsViewModel
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
    internal lateinit var tvShowsFeedAdapter: PopularTVShowsFeedAdapter

    private lateinit var moviesViewModel: PopularTVShowsViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        moviesViewModel =
            ViewModelProviders.of(this, viewModelProviderFactory).get(PopularTVShowsViewModel::class.java)

        moviesViewModel.getPopularTvShowsLiveData().observeNonNull(this) {
            renderPopularTVShows(it)
        }

        savedInstanceState.runIfNull {
            fetchMovies(FIRST_PAGE)
        }
        initPopularTVShowsRecyclerView()
        initMaterialDialog()
    }

    private fun initPopularTVShowsRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.apply {
            adapter = tvShowsFeedAdapter
            layoutManager = linearLayoutManager
            addOnScrollListener(object : EndlessScrollListener(linearLayoutManager) {
                override fun onLoadMore(page: Int) {
                    fetchMovies(page)
                }
            })
        }
    }

    private fun renderPopularTVShows(feedViewState: PopularTVShowsFeedViewState) {
        with(binding) {
            viewState = feedViewState
            executePendingBindings()
        }
        tvShowsFeedAdapter.setTvShows(feedViewState.getPopularTvShows())
    }

    private fun fetchMovies(page: Int) {
        moviesViewModel.fetchMovies(page)
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