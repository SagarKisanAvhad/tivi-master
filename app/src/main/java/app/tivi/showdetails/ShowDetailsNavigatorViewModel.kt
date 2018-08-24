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

package app.tivi.showdetails

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import app.tivi.AppNavigator
import app.tivi.SharedElementHelper
import app.tivi.data.entities.Episode
import app.tivi.data.entities.TiviShow
import app.tivi.util.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Provider

class ShowDetailsNavigatorViewModel @Inject constructor(
        private val appNavigatorProvider: Provider<AppNavigator>
) : ViewModel(), ShowDetailsNavigator {
    override fun showShowDetails(show: TiviShow, sharedElements: SharedElementHelper?) {
        if (show.id != null) {
            appNavigatorProvider.get().startShowDetails(show.id!!, sharedElements)
        }
    }

    override fun showEpisodeDetails(episode: Episode) {
        _events.value = ShowEpisodeDetailsEvent(episode.id!!)
    }

    override fun navigateUp() {
        _events.value = NavigateUpEvent
    }

    private val _events = SingleLiveEvent<ShowDetailsNavigatorEvent>(errorOnNoObservers = true)
    val events: LiveData<ShowDetailsNavigatorEvent>
        get() = _events
}

sealed class ShowDetailsNavigatorEvent
object NavigateUpEvent : ShowDetailsNavigatorEvent()
data class ShowEpisodeDetailsEvent(val episodeId: Long) : ShowDetailsNavigatorEvent()