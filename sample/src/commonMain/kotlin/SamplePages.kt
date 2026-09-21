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
            content = {
                preferenceCategory(key = "basic_category", title = "Basic")
                preference(
                    key = "preference",
                    title = "Preference",
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = null,
                        )
                    },
                    summary = "Summary",
                ) {}
                preference(
                    key = "preference_without_icon",
                    title = "Preference without icon",
                    summary = "Summary",
                ) {}
                preferenceCategory(
                    key = "checkbox_category",
                    title = "Checkbox",
                )
                checkboxPreference(
                    key = "checkbox_preference",
                    defaultValue = false,
                    title = "Checkbox preference",
                    summary = { if (it) "On" else "Off" },
                    staticSummary = "On/Off",
                )
                checkboxPreference(
                    key = "disabled_checkbox_preference",
                    defaultValue = true,
                    title = "Disabled checkbox preference",
                    enabled = { false },
                    summary = { if (it) "On" else "Off" },
                    staticSummary = "On/Off",
                )
                preferenceCategory(key = "switch_category", title = "Switch")
                switchPreference(
                    key = "switch_preference",
                    defaultValue = false,
                    title = "Switch preference",
                    summary = { if (it) "On" else "Off" },
                    staticSummary = "On/Off",
                )
                twoTargetSwitchPreference(
                    key = "two_target_switch_preference",
                    defaultValue = false,
                    title = "Two target switch preference",
                    summary = { if (it) "On" else "Off" },
                    staticSummary = "On/Off",
                ) {}
                twoTargetIconButtonPreference(
                    key = "two_target_icon_button_preference",
                    title = "Two target icon button preference",
                    summary = "Summary",
                    onClick = {},
                    iconButtonIcon = {
                        Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings")
                    },
                ) {}
                preferenceCategory(
                    key = "radio_category",
                    title = "Radio button",
                )
                radioButtonPreference(
                    key = "radio_button_preference",
                    selected = true,
                    title = "Radio button preference",
                    summary = "Summary",
                ) {}
            },
        ),
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
        ),
        PreferencePage(
            id = "cards",
            title = "Preference cards",
            summary = "Preference cards in a Material Design 3 card",
            content = {
                preferenceCategory(key = "card_filled_category", title = "Filled")
                preferenceCard(key = "card_filled") {
                    preference(
                        title = "Filled card",
                        summary = "Default card style and colors",
                    )
                    preference(
                        title = "Filled card (second item)",
                        summary = "Cards can group multiple preferences",
                    )
                }
                preferenceCard(
                    key = "card_filled_color",
                    cardColor = colorScheme.primaryContainer,
                ) {
                    preference(
                        title = "Filled card (custom color)",
                        summary = "primaryContainer as card color",
                    )
                }
                preferenceCard(
                    key = "card_filled_shape",
                    shape = RoundedCornerShape(24.dp),
                ) {
                    preference(
                        title = "Filled card (custom shape)",
                        summary = "RoundedCornerShape(24.dp)",
                    )
                }
                preferenceCategory(
                    key = "card_elevated_category",
                    title = "Elevated",
                )
                preferenceCard(key = "card_elevated", style = PreferenceCardStyle.Elevated) {
                    preference(
                        title = "Elevated card",
                        summary = "Default elevation",
                    )
                }
                preferenceCard(
                    key = "card_elevated_custom",
                    style = PreferenceCardStyle.Elevated,
                    cardColor = colorScheme.secondaryContainer,
                ) {
                    preference(
                        title = "Elevated card (custom color)",
                        summary = "secondaryContainer as card color",
                    )
                }
                preferenceCategory(
                    key = "card_outlined_category",
                    title = "Outlined",
                )
                preferenceCard(key = "card_outlined", style = PreferenceCardStyle.Outlined) {
                    preference(
                        title = "Outlined card",
                        summary = "Default outline border",
                    )
                }
                preferenceCard(
                    key = "card_outlined_custom",
                    style = PreferenceCardStyle.Outlined,
                    cardColor = colorScheme.surfaceVariant,
                ) {
                    preference(
                        title = "Outlined card (custom color)",
                        summary = "surfaceVariant as card color",
                    )
                }
                preferenceCategory(
                    key = "card_padding_category",
                    title = "Padding",
                )
                preferenceCard(
                    key = "card_no_outer_padding",
                    outerPadding = PaddingValues(0.dp),
                ) {
                    preference(
                        title = "No outer padding",
                        summary = "outerPadding = PaddingValues(0.dp)",
                    )
                }
                preferenceCard(
                    key = "card_extra_outer_padding",
                    outerPadding = PaddingValues(32.dp),
                ) {
                    preference(
                        title = "Extra outer padding",
                        summary = "outerPadding = PaddingValues(32.dp)",
                    )
                }
                preferenceCard(key = "card_extra_padding", contentPadding = PaddingValues(32.dp)) {
                    preference(
                        title = "Extra content padding",
                        summary = "contentPadding = PaddingValues(32.dp)",
                    )
                }
                preferenceCategory(
                    key = "card_item_style_category",
                    title = "Item style",
                )
                preferenceCardGroup(key = "card_individual_items") {
                    card {
                        Preference(
                            title = "Individual items",
                            summary = "First card has rounded top corners",
                        )
                    }
                    card {
                        Preference(
                            title = "Individual items (middle)",
                            summary = "Middle cards have square corners",
                        )
                    }
                    card {
                        Preference(
                            title = "Individual items (last)",
                            summary = "Last card has rounded bottom corners",
                        )
                    }
                }
                preferenceCard(key = "card_item_spacing", itemSpacing = 4.dp) {
                    preference(
                        title = "Item spacing",
                        summary = "itemSpacing = 4.dp",
                    )
                    preference(
                        title = "Item spacing (second)",
                        summary = "Gap between items",
                    )
                }
                preferenceCard {
                    preference(
                        title = "Keyless card",
                        summary = "No key needed in the lazy list",
                    )
                }
            },
        ),
    )
}
