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

package app.tivi.ui

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import app.tivi.R

object RoundRectViewOutline : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        val radius = view.resources.getDimension(R.dimen.image_round_rect_radius)
        outline.setRoundRect(0, 0, view.width, view.height, radius)
    }
}