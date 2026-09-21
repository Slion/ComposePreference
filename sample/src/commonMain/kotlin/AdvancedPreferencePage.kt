/*
 * Copyright 2026 Stéphane Lenclud
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
import net.slions.compose.preference.ListPreferenceType
import net.slions.compose.preference.PreferencePage
import net.slions.compose.preference.footerPreference
import net.slions.compose.preference.listPreference
import net.slions.compose.preference.multiSelectListPreference
import net.slions.compose.preference.preferenceCategory
import net.slions.compose.preference.sliderPreference
import net.slions.compose.preference.textFieldPreference
import kotlin.math.roundToInt

/** The advanced preferences page. */
@Composable
fun advancedPreferencePage(): PreferencePage =
    PreferencePage(
        id = "advanced",
        title = "Advanced preferences",
        summary = "Slider, list, multi-select and text field rows",
        content = {
            preferenceCategory(key = "slider_category", title = "Slider")
            sliderPreference(
                key = "slider_preference",
                defaultValue = 0f,
                title = "Slider preference",
                valueRange = 0f..5f,
                valueSteps = 9,
                summary = { "Summary" },
                staticSummary = "Summary",
                valueText = { ((it / 0.5f).roundToInt() * 0.5f).toString() },
            )
            preferenceCategory(key = "list_category", title = "List")
            listPreference(
                key = "list_alert_dialog_preference",
                defaultValue = "Alpha",
                values = listOf("Alpha", "Beta", "Canary"),
                title = "List preference (alert dialog)",
                summary = {
                    it
                },
            )
            listPreference(
                key = "list_dropdown_menu_preference",
                defaultValue = "Alpha",
                values = listOf("Alpha", "Beta", "Canary"),
                title = "List preference (dropdown menu)",
                summary = {
                    it
                },
                type = ListPreferenceType.DROPDOWN_MENU,
            )
            multiSelectListPreference(
                key = "multi_select_list_preference",
                defaultValue = setOf("Alpha", "Beta"),
                values = listOf("Alpha", "Beta", "Canary"),
                title = "Multi-select list preference",
                summary = { it.sorted().joinToString(", ") },
            )
            preferenceCategory(
                key = "text_field_category",
                title = "Text field",
            )
            textFieldPreference(
                key = "text_field_preference",
                defaultValue = "Value",
                title = "Text field preference",
                textToValue = { it },
                summary = {
                    it
                },
            )
            footerPreference(
                key = "footer_preference",
                title = "Footer preference",
                summary = "Footer preference summary",
            )
        },
    )
