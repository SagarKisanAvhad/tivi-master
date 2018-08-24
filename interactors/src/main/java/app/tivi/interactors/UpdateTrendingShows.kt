/*
 * Copyright 2018 Google, Inc.
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

package app.tivi.interactors

import android.arch.paging.DataSource
import app.tivi.data.repositories.trendingshows.TrendingShowsRepository
import app.tivi.data.resultentities.TrendingEntryWithShow
import app.tivi.extensions.emptyFlowableList
import app.tivi.interactors.UpdateTrendingShows.ExecuteParams
import app.tivi.util.AppCoroutineDispatchers
import app.tivi.util.AppRxSchedulers
import io.reactivex.Flowable
import kotlinx.coroutines.experimental.CoroutineDispatcher
import javax.inject.Inject

class UpdateTrendingShows @Inject constructor(
        dispatchers: AppCoroutineDispatchers,
        private val schedulers: AppRxSchedulers,
        private val trendingShowsRepository: TrendingShowsRepository
) : PagingInteractor<TrendingEntryWithShow>, SubjectInteractor<Unit, ExecuteParams, List<TrendingEntryWithShow>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    init {
        // We don't have params, so lets set Unit to kick off the observable
        setParams(Unit)
    }

    override fun dataSourceFactory(): DataSource.Factory<Int, TrendingEntryWithShow> {
        return trendingShowsRepository.observeForPaging()
    }

    override fun createObservable(params: Unit): Flowable<List<TrendingEntryWithShow>> {
        return trendingShowsRepository.observeForFlowable()
                .startWith(emptyFlowableList())
                .subscribeOn(schedulers.io)
    }

    override suspend fun execute(params: Unit, executeParams: ExecuteParams) {
        when (executeParams.page) {
            Page.NEXT_PAGE -> trendingShowsRepository.loadNextPage()
            Page.REFRESH -> trendingShowsRepository.refresh()
        }
    }

    data class ExecuteParams(val page: Page)

    enum class Page {
        NEXT_PAGE, REFRESH
    }
}
