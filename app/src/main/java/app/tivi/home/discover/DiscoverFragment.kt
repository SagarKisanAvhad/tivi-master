/*
 * Copyright 2017 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.tivi.home.discover

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import app.tivi.R
import app.tivi.data.Entry
import app.tivi.data.entities.TiviShow
import app.tivi.data.resultentities.EntryWithShow
import app.tivi.data.resultentities.PopularEntryWithShow
import app.tivi.data.resultentities.TrendingEntryWithShow
import app.tivi.databinding.FragmentDiscoverBinding
import app.tivi.extensions.observeNotNull
import app.tivi.home.HomeFragment
import app.tivi.home.HomeNavigator
import app.tivi.home.HomeNavigatorViewModel
import app.tivi.ui.ListItemSharedElementHelper
import app.tivi.ui.SpacingItemDecorator
import app.tivi.util.GridToGridTransitioner

internal class DiscoverFragment : HomeFragment<DiscoverViewModel>() {
    private lateinit var binding: FragmentDiscoverBinding
    private lateinit var searchView: SearchView
    private lateinit var listItemSharedElementHelper: ListItemSharedElementHelper

    private lateinit var homeNavigator: HomeNavigator

    private val controller = DiscoverEpoxyController(object : DiscoverEpoxyController.Callbacks {
        override fun onTrendingHeaderClicked(items: List<TrendingEntryWithShow>) {
            viewModel.onTrendingHeaderClicked(homeNavigator,
                    listItemSharedElementHelper.createForItems(items))
        }

        override fun onPopularHeaderClicked(items: List<PopularEntryWithShow>) {
            viewModel.onPopularHeaderClicked(homeNavigator,
                    listItemSharedElementHelper.createForItems(items))
        }

        override fun onItemClicked(viewHolderId: Long, item: EntryWithShow<out Entry>) {
            viewModel.onItemPosterClicked(homeNavigator, item.show,
                    listItemSharedElementHelper.createForId(viewHolderId, "poster"))
        }

        override fun onSearchItemClicked(viewHolderId: Long, item: TiviShow) {
            viewModel.onItemPosterClicked(homeNavigator, item,
                    listItemSharedElementHelper.createForId(viewHolderId, "poster"))
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DiscoverViewModel::class.java)
        homeNavigator = ViewModelProviders.of(activity!!, viewModelFactory).get(HomeNavigatorViewModel::class.java)

        GridToGridTransitioner.setupFirstFragment(this,
                intArrayOf(R.id.summary_appbarlayout, R.id.summary_status_scrim))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.data.observeNotNull(this) { model ->
            binding.state = model
            controller.setData(model)
            scheduleStartPostponedTransitions()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        binding.summaryRv.apply {
            setController(controller)
            addItemDecoration(SpacingItemDecorator(paddingLeft))
        }

        listItemSharedElementHelper = ListItemSharedElementHelper(binding.summaryRv)

        binding.summaryToolbar.apply {
            inflateMenu(R.menu.discover_toolbar)
            setOnMenuItemClickListener(this@DiscoverFragment::onMenuItemClicked)

            val searchItem = menu.findItem(R.id.discover_search)
            searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    viewModel.onSearchOpened()
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    viewModel.onSearchClosed()
                    return true
                }
            })

            searchView = menu.findItem(R.id.discover_search).actionView as SearchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.onSearchQueryChanged(query)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                viewModel.onSearchQueryChanged(query)
                return true
            }
        })

        binding.summarySwipeRefresh.setOnRefreshListener(viewModel::refresh)
    }

    override fun getMenu(): Menu? = binding.summaryToolbar.menu

    internal fun scrollToTop() {
        binding.summaryRv.apply {
            stopScroll()
            smoothScrollToPosition(0)
        }
        binding.summaryAppbarlayout.setExpanded(true)
    }
}
