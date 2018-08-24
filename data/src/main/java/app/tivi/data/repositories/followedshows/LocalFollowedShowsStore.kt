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

package app.tivi.data.repositories.followedshows

import android.arch.paging.DataSource
import app.tivi.data.DatabaseTransactionRunner
import app.tivi.data.daos.EntityInserter
import app.tivi.data.daos.FollowedShowsDao
import app.tivi.data.daos.LastRequestDao
import app.tivi.data.daos.TiviShowDao
import app.tivi.data.entities.FollowedShowEntry
import app.tivi.data.entities.PendingAction
import app.tivi.data.entities.Request
import app.tivi.data.resultentities.FollowedShowEntryWithShow
import app.tivi.data.syncers.syncerForEntity
import io.reactivex.Flowable
import org.threeten.bp.temporal.TemporalAmount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalFollowedShowsStore @Inject constructor(
        private val transactionRunner: DatabaseTransactionRunner,
        private val entityInserter: EntityInserter,
        private val followedShowsDao: FollowedShowsDao,
        private val showDao: TiviShowDao,
        private val lastRequestDao: LastRequestDao
) {
    var traktListId: Int? = null

    private val syncer = syncerForEntity(
            followedShowsDao,
            { showDao.getTraktIdForShowId(it.showId)!! },
            { entity, id -> entity.copy(id = id) }
    )

    fun getEntryForShowId(showId: Long): FollowedShowEntry? = followedShowsDao.entryWithShowId(showId)

    fun getEntries(): List<FollowedShowEntry> = followedShowsDao.entries()

    fun getEntriesWithAddAction() = followedShowsDao.entriesWithSendPendingActions()

    fun getEntriesWithDeleteAction() = followedShowsDao.entriesWithDeletePendingActions()

    fun updateEntriesWithAction(ids: List<Long>, action: PendingAction): Int {
        return followedShowsDao.updateEntriesToPendingAction(ids, action.value)
    }

    fun deleteEntriesInIds(ids: List<Long>) = followedShowsDao.deleteWithIds(ids)

    fun observeForPaging(): DataSource.Factory<Int, FollowedShowEntryWithShow> = followedShowsDao.entriesDataSource()

    fun observeIsShowFollowed(showId: Long): Flowable<Boolean> {
        return followedShowsDao.entryCountWithShowIdNotPendingDeleteFlowable(showId)
                .map { it > 0 }
    }

    fun isShowFollowed(showId: Long) = followedShowsDao.entryCountWithShowId(showId) > 0

    fun sync(entities: List<FollowedShowEntry>) = transactionRunner {
        syncer.sync(followedShowsDao.entries(), entities)
    }

    fun updateLastFollowedShowsSync() {
        lastRequestDao.updateLastRequest(Request.FOLLOWED_SHOWS, 0)
    }

    fun isLastFollowedShowsSyncBefore(threshold: TemporalAmount): Boolean {
        return lastRequestDao.isRequestBefore(Request.FOLLOWED_SHOWS, 0, threshold)
    }

    fun save(entry: FollowedShowEntry) {
        entityInserter.insertOrUpdate(followedShowsDao, entry)
    }
}