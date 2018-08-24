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

package app.tivi.data.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.tivi.data.entities.TraktUser
import io.reactivex.Flowable

@Dao
interface UserDao : EntityDao<TraktUser> {
    @Query("SELECT * FROM users WHERE is_me != 0")
    fun observeMe(): Flowable<TraktUser>

    @Query("SELECT * FROM users WHERE username = :username")
    fun observeTraktUser(username: String): Flowable<TraktUser>

    @Query("SELECT * FROM users WHERE username = :username")
    fun getTraktUser(username: String): TraktUser?

    @Query("SELECT * FROM users WHERE is_me != 0")
    fun getMe(): TraktUser?

    @Query("SELECT id FROM users WHERE username = :username")
    fun getIdForUsername(username: String): Long?

    @Query("SELECT id FROM users WHERE is_me != 0")
    fun getIdForMe(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(entity: TraktUser): Long

    @Query("DELETE FROM users")
    fun deleteAll()
}