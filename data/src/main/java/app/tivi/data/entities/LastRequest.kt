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

package app.tivi.data.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.Instant

@Entity(
        tableName = "last_requests",
        indices = [Index(value = ["request", "entity_id"], unique = true)]
)
data class LastRequest(
        @PrimaryKey @ColumnInfo(name = "id") override val id: Long? = null,
        @ColumnInfo(name = "request") val request: Request,
        @ColumnInfo(name = "entity_id") val entityId: Long,
        @ColumnInfo(name = "timestamp") val timestamp: Instant
) : TiviEntity
