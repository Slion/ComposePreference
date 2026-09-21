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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import net.slions.compose.preference.Preference
import net.slions.compose.preference.PreferenceCardStyle
import net.slions.compose.preference.PreferencePage
import net.slions.compose.preference.preferenceCategory
import net.slions.compose.preference.preferenceCard
import net.slions.compose.preference.preferenceCardGroup

/** The preference cards page. */
@Composable
fun cardPreferencePage(): PreferencePage {
    val colorScheme = MaterialTheme.colorScheme
    return PreferencePage(
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
    )
}
