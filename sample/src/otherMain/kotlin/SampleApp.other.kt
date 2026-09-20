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

package me.zhanghai.compose.preference.sample

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.zhanghai.compose.preference.preferenceCategory

/**
 * The non-Android fallback: [PreferencePageScreen] is Android-only, so the pages are shown
 * as a single flat list.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
actual fun SampleApp() {
    SampleTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text(text = SampleTitle) }) },
        ) { contentPadding ->
            val pages = samplePages()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
            ) {
                pages.forEach { page ->
                    preferenceCategory(
                        key = "page_${page.id}",
                        title = { Text(text = page.title) },
                    )
                    page.content(this)
                }
            }
        }
    }
}
