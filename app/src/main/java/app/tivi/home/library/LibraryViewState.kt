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

package app.tivi.home.library

import android.arch.paging.PagedList
import app.tivi.data.resultentities.FollowedShowEntryWithShow
import app.tivi.data.resultentities.WatchedShowEntryWithShow
import app.tivi.tmdb.TmdbImageUrlProvider

sealed class LibraryViewState(
        open val allowedFilters: List<LibraryFilter>,
        open val filter: LibraryFilter,
        open val tmdbImageUrlProvider: TmdbImageUrlProvider,
        open val isLoading: Boolean,
        open val isEmpty: Boolean
)

data class LibraryFollowedViewState(
        override val allowedFilters: List<LibraryFilter>,
        override val filter: LibraryFilter,
        override val tmdbImageUrlProvider: TmdbImageUrlProvider,
        override val isLoading: Boolean,
        override val isEmpty: Boolean,
        val followedShows: PagedList<FollowedShowEntryWithShow>
) : LibraryViewState(allowedFilters, filter, tmdbImageUrlProvider, isLoading, isEmpty)

data class LibraryWatchedViewState(
        override val allowedFilters: List<LibraryFilter>,
        override val filter: LibraryFilter,
        override val tmdbImageUrlProvider: TmdbImageUrlProvider,
        override val isLoading: Boolean,
        override val isEmpty: Boolean,
        val watchedShows: PagedList<WatchedShowEntryWithShow>
) : LibraryViewState(allowedFilters, filter, tmdbImageUrlProvider, isLoading, isEmpty)