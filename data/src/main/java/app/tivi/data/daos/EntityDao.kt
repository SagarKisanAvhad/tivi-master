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

package app.tivi.data.daos

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Update
import app.tivi.data.entities.TiviEntity

interface EntityDao<in E : TiviEntity> {
    @Insert
    fun insert(entity: E): Long

    @Insert
    fun insertAll(vararg entity: E)

    @Insert
    fun insertAll(entities: List<E>)

    @Update
    fun update(entity: E)

    @Delete
    fun delete(entity: E): Int
}