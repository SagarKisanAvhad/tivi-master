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

package app.tivi.home.trending

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import app.tivi.PosterGridItemBindingModel_
import app.tivi.R
import app.tivi.SharedElementHelper
import app.tivi.data.resultentities.TrendingEntryWithShow
import app.tivi.home.HomeNavigator
import app.tivi.home.HomeNavigatorViewModel
import app.tivi.util.EntryGridEpoxyController
import app.tivi.util.EntryGridFragment
import kotlinx.android.synthetic.main.fragment_rv_grid.*

class TrendingShowsFragment : EntryGridFragment<TrendingEntryWithShow, TrendingShowsViewModel>(TrendingShowsViewModel::class.java) {

    private lateinit var homeNavigator: HomeNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeNavigator = ViewModelProviders.of(activity!!, viewModelFactory).get(HomeNavigatorViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        grid_toolbar.apply {
            title = getString(R.string.discover_trending)
            setNavigationOnClickListener {
                viewModel.onUpClicked(homeNavigator)
            }
        }
    }

    override fun createController(): EntryGridEpoxyController<TrendingEntryWithShow> {
        return object : EntryGridEpoxyController<TrendingEntryWithShow>() {
            override fun buildItemModel(item: TrendingEntryWithShow): PosterGridItemBindingModel_ {
                return super.buildItemModel(item)
                        .annotationLabel(item.entry?.watchers.toString())
                        .annotationIcon(R.drawable.ic_eye_12dp)
            }
        }
    }

    override fun onItemClicked(item: TrendingEntryWithShow) {
        val sharedElements = SharedElementHelper()
        grid_recyclerview.findViewHolderForItemId(item.generateStableId())?.let {
            sharedElements.addSharedElement(it.itemView, "poster")
        }
        viewModel.onItemClicked(item, homeNavigator, sharedElements)
    }
}
