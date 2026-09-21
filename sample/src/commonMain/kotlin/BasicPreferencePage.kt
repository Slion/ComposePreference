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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import net.slions.compose.preference.PreferencePage
import net.slions.compose.preference.checkboxPreference
import net.slions.compose.preference.preference
import net.slions.compose.preference.preferenceCategory
import net.slions.compose.preference.radioButtonPreference
import net.slions.compose.preference.switchPreference
import net.slions.compose.preference.twoTargetIconButtonPreference
import net.slions.compose.preference.twoTargetSwitchPreference

/** The basic preferences page. */
@Composable
fun basicPreferencePage(): PreferencePage =
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
    )
