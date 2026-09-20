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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import net.slions.compose.preference.ListPreferenceType
import net.slions.compose.preference.Preference
import net.slions.compose.preference.PreferenceCardStyle
import net.slions.compose.preference.PreferencePage
import net.slions.compose.preference.PreferenceSearchEntry
import net.slions.compose.preference.checkboxPreference
import net.slions.compose.preference.footerPreference
import net.slions.compose.preference.listPreference
import net.slions.compose.preference.multiSelectListPreference
import net.slions.compose.preference.preference
import net.slions.compose.preference.preferenceCategory
import net.slions.compose.preference.preferenceCard
import net.slions.compose.preference.preferenceCardGroup
import net.slions.compose.preference.radioButtonPreference
import net.slions.compose.preference.sliderPreference
import net.slions.compose.preference.switchPreference
import net.slions.compose.preference.textFieldPreference
import net.slions.compose.preference.twoTargetIconButtonPreference
import net.slions.compose.preference.twoTargetSwitchPreference
import kotlin.math.roundToInt

const val SampleTitle = "ComposePreference Sample"

/**
 * The sample pages: one page per preference type, hosted by
 * [net.slions.compose.preference.PreferencePageScreen].
 */
@Composable
fun samplePages(): List<PreferencePage> {
    val colorScheme = MaterialTheme.colorScheme
    return listOf(
        PreferencePage(
            id = "basic",
            title = "Basic preferences",
            summary = "Simple, checkbox, switch and two-target rows",
            searchEntries =
                listOf(
                    PreferenceSearchEntry("preference", "Preference", "Summary"),
                    PreferenceSearchEntry(
                        "preference_without_icon",
                        "Preference without icon",
                        "Summary",
                    ),
                    PreferenceSearchEntry("checkbox_preference", "Checkbox preference"),
                    PreferenceSearchEntry(
                        "disabled_checkbox_preference",
                        "Disabled checkbox preference",
                    ),
                    PreferenceSearchEntry("switch_preference", "Switch preference"),
                    PreferenceSearchEntry(
                        "two_target_switch_preference",
                        "Two target switch preference",
                    ),
                    PreferenceSearchEntry(
                        "two_target_icon_button_preference",
                        "Two target icon button preference",
                    ),
                    PreferenceSearchEntry("radio_button_preference", "Radio button preference"),
                ),
            content = {
                preferenceCategory(key = "basic_category", title = { Text(text = "Basic") })
                preference(
                    key = "preference",
                    title = { Text(text = "Preference") },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = null,
                        )
                    },
                    summary = { Text(text = "Summary") },
                ) {}
                preference(
                    key = "preference_without_icon",
                    title = { Text(text = "Preference without icon") },
                    summary = { Text(text = "Summary") },
                ) {}
                preferenceCategory(
                    key = "checkbox_category",
                    title = { Text(text = "Checkbox") },
                )
                checkboxPreference(
                    key = "checkbox_preference",
                    defaultValue = false,
                    title = { Text(text = "Checkbox preference") },
                    summary = { Text(text = if (it) "On" else "Off") },
                )
                checkboxPreference(
                    key = "disabled_checkbox_preference",
                    defaultValue = true,
                    title = { Text(text = "Disabled checkbox preference") },
                    enabled = { false },
                    summary = { Text(text = if (it) "On" else "Off") },
                )
                preferenceCategory(key = "switch_category", title = { Text(text = "Switch") })
                switchPreference(
                    key = "switch_preference",
                    defaultValue = false,
                    title = { Text(text = "Switch preference") },
                    summary = { Text(text = if (it) "On" else "Off") },
                )
                twoTargetSwitchPreference(
                    key = "two_target_switch_preference",
                    defaultValue = false,
                    title = { Text(text = "Two target switch preference") },
                    summary = { Text(text = if (it) "On" else "Off") },
                ) {}
                twoTargetIconButtonPreference(
                    key = "two_target_icon_button_preference",
                    title = { Text(text = "Two target icon button preference") },
                    summary = { Text(text = "Summary") },
                    onClick = {},
                    iconButtonIcon = {
                        Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings")
                    },
                ) {}
                preferenceCategory(
                    key = "radio_category",
                    title = { Text(text = "Radio button") },
                )
                radioButtonPreference(
                    key = "radio_button_preference",
                    selected = true,
                    title = { Text(text = "Radio button preference") },
                    summary = { Text(text = "Summary") },
                ) {}
            },
        ),
        PreferencePage(
            id = "advanced",
            title = "Advanced preferences",
            summary = "Slider, list, multi-select and text field rows",
            searchEntries =
                listOf(
                    PreferenceSearchEntry("slider_preference", "Slider preference"),
                    PreferenceSearchEntry(
                        "list_alert_dialog_preference",
                        "List preference (alert dialog)",
                    ),
                    PreferenceSearchEntry(
                        "list_dropdown_menu_preference",
                        "List preference (dropdown menu)",
                    ),
                    PreferenceSearchEntry(
                        "multi_select_list_preference",
                        "Multi-select list preference",
                    ),
                    PreferenceSearchEntry("text_field_preference", "Text field preference"),
                    PreferenceSearchEntry(
                        "footer_preference",
                        "Footer preference",
                        "Footer preference summary",
                    ),
                ),
            content = {
                preferenceCategory(key = "slider_category", title = { Text(text = "Slider") })
                sliderPreference(
                    key = "slider_preference",
                    defaultValue = 0f,
                    title = { Text(text = "Slider preference") },
                    valueRange = 0f..5f,
                    valueSteps = 9,
                    summary = { Text(text = "Summary") },
                    valueText = { Text(text = ((it / 0.5f).roundToInt() * 0.5f).toString()) },
                )
                preferenceCategory(key = "list_category", title = { Text(text = "List") })
                listPreference(
                    key = "list_alert_dialog_preference",
                    defaultValue = "Alpha",
                    values = listOf("Alpha", "Beta", "Canary"),
                    title = { Text(text = "List preference (alert dialog)") },
                    summary = { Text(text = it) },
                )
                listPreference(
                    key = "list_dropdown_menu_preference",
                    defaultValue = "Alpha",
                    values = listOf("Alpha", "Beta", "Canary"),
                    title = { Text(text = "List preference (dropdown menu)") },
                    summary = { Text(text = it) },
                    type = ListPreferenceType.DROPDOWN_MENU,
                )
                multiSelectListPreference(
                    key = "multi_select_list_preference",
                    defaultValue = setOf("Alpha", "Beta"),
                    values = listOf("Alpha", "Beta", "Canary"),
                    title = { Text(text = "Multi-select list preference") },
                    summary = { Text(text = it.sorted().joinToString(", ")) },
                )
                preferenceCategory(
                    key = "text_field_category",
                    title = { Text(text = "Text field") },
                )
                textFieldPreference(
                    key = "text_field_preference",
                    defaultValue = "Value",
                    title = { Text(text = "Text field preference") },
                    textToValue = { it },
                    summary = { Text(text = it) },
                )
                footerPreference(
                    key = "footer_preference",
                    summary = { Text(text = "Footer preference summary") },
                )
            },
        ),
        PreferencePage(
            id = "cards",
            title = "Preference cards",
            summary = "Preference cards in a Material Design 3 card",
            searchEntries =
                listOf(
                    PreferenceSearchEntry(
                        "card_filled",
                        "Filled card",
                        "Default card style and colors",
                    ),
                    PreferenceSearchEntry(
                        "card_filled_color",
                        "Filled card (custom color)",
                    ),
                    PreferenceSearchEntry(
                        "card_filled_shape",
                        "Filled card (custom shape)",
                    ),
                    PreferenceSearchEntry("card_elevated", "Elevated card"),
                    PreferenceSearchEntry("card_elevated_custom", "Elevated card (custom color)"),
                    PreferenceSearchEntry("card_outlined", "Outlined card"),
                    PreferenceSearchEntry("card_outlined_custom", "Outlined card (custom color)"),
                    PreferenceSearchEntry(
                        "card_no_outer_padding",
                        "No outer padding",
                    ),
                    PreferenceSearchEntry(
                        "card_extra_outer_padding",
                        "Extra outer padding",
                    ),
                    PreferenceSearchEntry("card_extra_padding", "Extra content padding"),
                    PreferenceSearchEntry("card_individual_items", "Individual items"),
                    PreferenceSearchEntry("card_item_spacing", "Item spacing"),
                ),
            content = {
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
                preferenceCard(key = "card_extra_padding", contentPadding = PaddingValues(32.dp)) {
                    Preference(
                        title = { Text(text = "Extra content padding") },
                        summary = { Text(text = "contentPadding = PaddingValues(32.dp)") },
                    )
                }
                preferenceCategory(
                    key = "card_item_style_category",
                    title = { Text(text = "Item style") },
                )
                preferenceCardGroup(key = "card_individual_items") {
                    card {
                        Preference(
                            title = { Text(text = "Individual items") },
                            summary = { Text(text = "First card has rounded top corners") },
                        )
                    }
                    card {
                        Preference(
                            title = { Text(text = "Individual items (middle)") },
                            summary = { Text(text = "Middle cards have square corners") },
                        )
                    }
                    card {
                        Preference(
                            title = { Text(text = "Individual items (last)") },
                            summary = { Text(text = "Last card has rounded bottom corners") },
                        )
                    }
                }
                preferenceCard(key = "card_item_spacing", itemSpacing = 4.dp) {
                    Preference(
                        title = { Text(text = "Item spacing") },
                        summary = { Text(text = "itemSpacing = 4.dp") },
                    )
                    Preference(
                        title = { Text(text = "Item spacing (second)") },
                        summary = { Text(text = "Gap between items") },
                    )
                }
                preferenceCard {
                    Preference(
                        title = { Text(text = "Keyless card") },
                        summary = { Text(text = "No key needed in the lazy list") },
                    )
                }
            },
        ),
    )
}
