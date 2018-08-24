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

import app.tivi.data.repositories.relatedshows.RelatedShowsRepository
import app.tivi.data.resultentities.RelatedShowEntryWithShow
import app.tivi.extensions.emptyFlowableList
import app.tivi.interactors.UpdateRelatedShows.ExecuteParams
import app.tivi.interactors.UpdateRelatedShows.Params
import app.tivi.util.AppCoroutineDispatchers
import app.tivi.util.AppRxSchedulers
import io.reactivex.Flowable
import kotlinx.coroutines.experimental.CoroutineDispatcher
import javax.inject.Inject

class UpdateRelatedShows @Inject constructor(
        private val dispatchers: AppCoroutineDispatchers,
        private val schedulers: AppRxSchedulers,
        private val repository: RelatedShowsRepository
) : SubjectInteractor<Params, ExecuteParams, List<RelatedShowEntryWithShow>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override suspend fun execute(params: Params, executeParams: ExecuteParams) {
        repository.updateRelatedShows(params.showId)
    }

    override fun createObservable(params: Params): Flowable<List<RelatedShowEntryWithShow>> {
        return repository.observeRelatedShows(params.showId)
                .startWith(emptyFlowableList())
                .subscribeOn(schedulers.io)
    }

    data class Params(val showId: Long)
    data class ExecuteParams(val forceLoad: Boolean)
}