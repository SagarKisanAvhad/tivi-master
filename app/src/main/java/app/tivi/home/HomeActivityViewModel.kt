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

package app.tivi.home

import android.arch.lifecycle.LiveData
import app.tivi.trakt.TraktManager
import app.tivi.util.SingleLiveEvent
import app.tivi.util.TiviViewModel
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import javax.inject.Inject

internal class HomeActivityViewModel @Inject constructor(
        private val traktManager: TraktManager
) : TiviViewModel() {

    enum class NavigationItem {
        DISCOVER, LIBRARY
    }

    private val mutableNavLiveData = SingleLiveEvent<NavigationItem>()

    /**
     * Facade so that we don't leak the fact that its mutable
     */
    val navigationLiveData: LiveData<NavigationItem>
        get() = mutableNavLiveData

    init {
        // Set default value
        mutableNavLiveData.value = NavigationItem.DISCOVER
    }

    fun onNavigationItemClicked(item: NavigationItem) {
        mutableNavLiveData.value = item
    }

    fun onAuthResponse(
            authService: AuthorizationService,
            response: AuthorizationResponse?,
            ex: AuthorizationException?
    ) {
        when {
            response != null -> traktManager.onAuthResponse(authService, response)
            ex != null -> traktManager.onAuthException(ex)
        }
    }
}
