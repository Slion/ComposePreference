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

package me.zhanghai.compose.preference

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Style of the [PreferenceCard].
 */
public sealed class PreferenceCardStyle {
    /** A filled card (Material Design 3 [Card]) with [CardDefaults.cardColors]. */
    public object Filled : PreferenceCardStyle()

    /** An elevated card (Material Design 3 [ElevatedCard]) with
     * [CardDefaults.elevatedCardColors]. */
    public object Elevated : PreferenceCardStyle()

    /** An outlined card (Material Design 3 [OutlinedCard]) with
     * [CardDefaults.outlinedCardColors]. */
    public object Outlined : PreferenceCardStyle()
}

/**
 * Groups a set of preferences in a Material Design 3 card.
 *
 * @param modifier Modifier used to draw the card.
 * @param style Style of the card.
 * @param shape Shape of the card. If null, `MaterialTheme.shapes.medium` is used.
 * @param cardColor Card background color. If null, the default container color of [style] is
 * used.
 * @param cardElevation Card elevation. Only applies to [PreferenceCardStyle.Elevated]. If null,
 * [CardDefaults.elevatedCardElevation] is used.
 * @param cardBorder Card border. Only applies to [PreferenceCardStyle.Outlined]. If null,
 * [CardDefaults.outlinedCardBorder] is used.
 * @param outerPadding Clearance between the card and its container. If null,
 * `PreferenceTheme.horizontalSpacing` is used on all sides.
 * @param contentPadding Padding applied around the card content. If null, no padding is applied
 * (preferences already provide their own spacing).
 * @param content Content of the card, usually one or more preferences.
 */
@Composable
public fun PreferenceCard(
    modifier: Modifier = Modifier,
    style: PreferenceCardStyle = PreferenceCardStyle.Filled,
    shape: Shape? = null,
    cardColor: Color? = null,
    cardElevation: CardElevation? = null,
    cardBorder: BorderStroke? = null,
    outerPadding: PaddingValues? = null,
    contentPadding: PaddingValues? = null,
    content: @Composable () -> Unit,
) {
    val cardShape = shape ?: MaterialTheme.shapes.medium
    val outer = outerPadding ?: PaddingValues(LocalPreferenceTheme.current.horizontalSpacing)
    val cardPadding = contentPadding ?: PaddingValues(0.dp)
    Column(modifier = modifier.fillMaxWidth().padding(outer)) {
        when (style) {
            PreferenceCardStyle.Filled -> Card(
                modifier = Modifier.fillMaxWidth(),
                shape = cardShape,
                colors =
                    cardColor?.let { CardDefaults.cardColors(containerColor = it) }
                        ?: CardDefaults.cardColors(),
            ) {
                Column(modifier = Modifier.padding(cardPadding)) {
                    content()
                }
            }

            PreferenceCardStyle.Elevated -> ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = cardShape,
                colors =
                    cardColor?.let { CardDefaults.elevatedCardColors(containerColor = it) }
                        ?: CardDefaults.elevatedCardColors(),
                elevation = cardElevation ?: CardDefaults.elevatedCardElevation(),
            ) {
                Column(modifier = Modifier.padding(cardPadding)) {
                    content()
                }
            }

            PreferenceCardStyle.Outlined -> OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = cardShape,
                colors =
                    cardColor?.let { CardDefaults.outlinedCardColors(containerColor = it) }
                        ?: CardDefaults.outlinedCardColors(),
                border = cardBorder ?: CardDefaults.outlinedCardBorder(),
            ) {
                Column(modifier = Modifier.padding(cardPadding)) {
                    content()
                }
            }
        }
    }
}

/**
 * Adds a card of preferences to the lazy list.
 *
 * @param key Key used to identify the card in the lazy list. If null, no key is used.
 * @param modifier Modifier used to draw the card.
 * @param style Style of the card.
 * @param shape Shape of the card. If null, `MaterialTheme.shapes.medium` is used.
 * @param cardColor Card background color. If null, the default container color of [style] is
 * used.
 * @param cardElevation Card elevation. Only applies to [PreferenceCardStyle.Elevated]. If null,
 * [CardDefaults.elevatedCardElevation] is used.
 * @param cardBorder Card border. Only applies to [PreferenceCardStyle.Outlined]. If null,
 * [CardDefaults.outlinedCardBorder] is used.
 * @param outerPadding Clearance between the card and its container. If null,
 * `PreferenceTheme.horizontalSpacing` is used on all sides.
 * @param contentPadding Padding applied around the card content. If null, no padding is applied
 * (preferences already provide their own spacing).
 * @param content Content of the card, usually one or more preferences.
 */
public fun LazyListScope.preferenceCard(
    key: String? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    style: PreferenceCardStyle = PreferenceCardStyle.Filled,
    shape: Shape? = null,
    cardColor: Color? = null,
    cardElevation: CardElevation? = null,
    cardBorder: BorderStroke? = null,
    outerPadding: PaddingValues? = null,
    contentPadding: PaddingValues? = null,
    content: @Composable () -> Unit,
) {
    if (key != null) {
        item(key = key, contentType = "PreferenceCard") {
            PreferenceCard(
                modifier = modifier.then(highlightedKeyModifier(key)),
                style = style,
                shape = shape,
                cardColor = cardColor,
                cardElevation = cardElevation,
                cardBorder = cardBorder,
                outerPadding = outerPadding,
                contentPadding = contentPadding,
                content = content,
            )
        }
    } else {
        item(contentType = "PreferenceCard") {
            PreferenceCard(
                modifier = modifier,
                style = style,
                shape = shape,
                cardColor = cardColor,
                cardElevation = cardElevation,
                cardBorder = cardBorder,
                outerPadding = outerPadding,
                contentPadding = contentPadding,
                content = content,
            )
        }
    }
}
