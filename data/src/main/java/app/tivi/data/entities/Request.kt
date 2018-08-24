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

enum class Request(val tag: String) {
    SHOW_DETAILS("show_details"),
    SHOW_SEASONS("show_seasons"),
    EPISODE_DETAILS("episode_details"),
    SHOW_EPISODE_WATCHES("show_episode_watches"),
    FOLLOWED_SHOWS("followed_shows"),
    USER_PROFILE("user_profile"),
}