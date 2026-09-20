/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.slions.compose.preference.sample

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * The root of the sample app. On Android the pages are hosted by
 * [net.slions.compose.preference.PreferencePageScreen] (adaptive two-pane layout); on the
 * other targets a plain list is shown, since the page screen is Android-only.
 */
@Composable
expect fun SampleApp()

@Composable
@Preview
fun SampleAppPreview() {
    SampleApp()
}
