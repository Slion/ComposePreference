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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.PreferenceCardStyle
import me.zhanghai.compose.preference.PreferenceCategory
import me.zhanghai.compose.preference.preferenceCategory
import me.zhanghai.compose.preference.preferenceCard

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PreferenceCardsScreen(onBackClick: () -> Unit) {
    SampleBackHandler(onBack = onBackClick)
    val windowInsets = WindowInsets.safeDrawing
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val colorScheme = MaterialTheme.colorScheme
    Scaffold(
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = "Preference cards") },
                modifier = Modifier.fillMaxWidth(),
                windowInsets =
                    windowInsets.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = windowInsets,
    ) { contentPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = contentPadding) {
            preferenceCategory(key = "card_filled_category", title = { Text(text = "Filled") })
            preferenceCard(key = "card_filled") {
                Preference(
                    title = { Text(text = "Filled card") },
                    summary = { Text(text = "Default card style and colors") },
                )
                Preference(
                    title = { Text(text = "Filled card (second item)") },
                    summary = { Text(text = "Cards can group multiple preferences") },
                )
            }
            preferenceCard(
                key = "card_filled_color",
                cardColor = colorScheme.primaryContainer,
            ) {
                Preference(
                    title = { Text(text = "Filled card (custom color)") },
                    summary = { Text(text = "primaryContainer as card color") },
                )
            }
            preferenceCard(
                key = "card_filled_shape",
                shape = RoundedCornerShape(24.dp),
            ) {
                Preference(
                    title = { Text(text = "Filled card (custom shape)") },
                    summary = { Text(text = "RoundedCornerShape(24.dp)") },
                )
            }
            preferenceCategory(
                key = "card_elevated_category",
                title = { Text(text = "Elevated") },
            )
            preferenceCard(key = "card_elevated", style = PreferenceCardStyle.Elevated) {
                Preference(
                    title = { Text(text = "Elevated card") },
                    summary = { Text(text = "Default elevation") },
                )
            }
            preferenceCard(
                key = "card_elevated_custom",
                style = PreferenceCardStyle.Elevated,
                cardColor = colorScheme.secondaryContainer,
            ) {
                Preference(
                    title = { Text(text = "Elevated card (custom color)") },
                    summary = { Text(text = "secondaryContainer as card color") },
                )
            }
            preferenceCategory(
                key = "card_outlined_category",
                title = { Text(text = "Outlined") },
            )
            preferenceCard(key = "card_outlined", style = PreferenceCardStyle.Outlined) {
                Preference(
                    title = { Text(text = "Outlined card") },
                    summary = { Text(text = "Default outline border") },
                )
            }
            preferenceCard(
                key = "card_outlined_custom",
                style = PreferenceCardStyle.Outlined,
                cardColor = colorScheme.surfaceVariant,
            ) {
                Preference(
                    title = { Text(text = "Outlined card (custom color)") },
                    summary = { Text(text = "surfaceVariant as card color") },
                )
            }
            preferenceCategory(
                key = "card_padding_category",
                title = { Text(text = "Padding") },
            )
            preferenceCard(
                key = "card_default_content_padding",
                contentPadding =
                    PaddingValues(16.dp), // PreferenceTheme.horizontalSpacing
            ) {
                Preference(
                    title = { Text(text = "Content padding") },
                    summary = { Text(text = "contentPadding = PaddingValues(16.dp)") },
                )
            }
            preferenceCard(
                key = "card_extra_content_padding",
                contentPadding = PaddingValues(32.dp),
            ) {
                Preference(
                    title = { Text(text = "Extra content padding") },
                    summary = { Text(text = "contentPadding = PaddingValues(32.dp)") },
                )
            }
            preferenceCard(
                key = "card_no_outer_padding",
                outerPadding = PaddingValues(0.dp),
            ) {
                Preference(
                    title = { Text(text = "No outer padding") },
                    summary = { Text(text = "outerPadding = PaddingValues(0.dp)") },
                )
            }
            preferenceCard(
                key = "card_extra_outer_padding",
                outerPadding = PaddingValues(32.dp),
            ) {
                Preference(
                    title = { Text(text = "Extra outer padding") },
                    summary = { Text(text = "outerPadding = PaddingValues(32.dp)") },
                )
            }
            preferenceCard {
                Preference(
                    title = { Text(text = "Keyless card") },
                    summary = { Text(text = "No key needed in the lazy list") },
                )
            }
        }
    }
}
