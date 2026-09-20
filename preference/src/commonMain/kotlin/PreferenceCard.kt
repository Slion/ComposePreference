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

package net.slions.compose.preference

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.Dp
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
 * @param itemSpacing Vertical gap inserted between the card's top-level items. If 0.dp, no gap
 * is inserted.
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
    itemSpacing: Dp = 0.dp,
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
                CardContent(cardPadding, itemSpacing, content = content)
            }

            PreferenceCardStyle.Elevated -> ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = cardShape,
                colors =
                    cardColor?.let { CardDefaults.elevatedCardColors(containerColor = it) }
                        ?: CardDefaults.elevatedCardColors(),
                elevation = cardElevation ?: CardDefaults.elevatedCardElevation(),
            ) {
                CardContent(cardPadding, itemSpacing, content = content)
            }

            PreferenceCardStyle.Outlined -> OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = cardShape,
                colors =
                    cardColor?.let { CardDefaults.outlinedCardColors(containerColor = it) }
                        ?: CardDefaults.outlinedCardColors(),
                border = cardBorder ?: CardDefaults.outlinedCardBorder(),
            ) {
                CardContent(cardPadding, itemSpacing, content = content)
            }
        }
    }
}

/**
 * The card content, inserting [itemSpacing] gaps between the top-level items of [content].
 */
@Composable
private fun CardContent(
    contentPadding: PaddingValues,
    itemSpacing: Dp,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
    ) {
        content()
    }
}

/**
 * Scope for [PreferenceCardGroup]. Use [card] to add an item; each item is rendered in its own
 * [Card].
 */
public class PreferenceCardGroupScope {
    internal val items = mutableListOf<@Composable () -> Unit>()

    /** Adds an item to the group. Each item is drawn in its own [Card]. */
    public fun card(content: @Composable () -> Unit) {
        items.add(content)
    }
}

/**
 * Renders a group of preferences where each item is drawn in its own Material Design 3 [Card],
 * separated by [itemSpacing]. The first item shows rounded top corners, the last item shows
 * rounded bottom corners, and middle items have square corners, giving the impression of a
 * single card split into individual rows.
 *
 * @param modifier Modifier used to draw the group.
 * @param itemSpacing Gap between the individual cards.
 * @param shape Shape of the individual cards. If null, `MaterialTheme.shapes.medium` is used.
 * @param cardColor Card background color. If null, [CardDefaults.cardColors] is used.
 * @param outerPadding Clearance between the group and its container. If null,
 * `PreferenceTheme.horizontalSpacing` is used on all sides.
 * @param content Content of the group. Use [PreferenceCardGroupScope.card] to add items.
 */
@Composable
public fun PreferenceCardGroup(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 4.dp,
    shape: Shape? = null,
    cardColor: Color? = null,
    outerPadding: PaddingValues? = null,
    content: @Composable PreferenceCardGroupScope.() -> Unit,
) {
    val scope = PreferenceCardGroupScope()
    scope.content()
    val cardShape = shape ?: MaterialTheme.shapes.medium
    val cornerSize =
        (cardShape as? RoundedCornerShape ?: RoundedCornerShape(12.dp)).topStart
    val zero = CornerSize(0f)
    val outer = outerPadding ?: PaddingValues(LocalPreferenceTheme.current.horizontalSpacing)
    val colors =
        cardColor?.let { CardDefaults.cardColors(containerColor = it) }
            ?: CardDefaults.cardColors()
    val last = scope.items.size - 1
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(outer),
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
    ) {
        scope.items.forEachIndexed { index, item ->
            val itemShape =
                when {
                    last <= 0 -> cardShape
                    index == 0 -> RoundedCornerShape(cornerSize, cornerSize, zero, zero)
                    index == last -> RoundedCornerShape(zero, zero, cornerSize, cornerSize)
                    else -> RoundedCornerShape(zero)
                }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = itemShape,
                colors = colors,
            ) {
                item()
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
 * @param itemSpacing Vertical gap inserted between the card's top-level items. If 0.dp, no gap
 * is inserted.
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
    itemSpacing: Dp = 0.dp,
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
                itemSpacing = itemSpacing,
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
                itemSpacing = itemSpacing,
                content = content,
            )
        }
    }
}

/**
 * Adds a group of individual cards to the lazy list. Each item is drawn in its own [Card], with
 * the first showing rounded top corners and the last showing rounded bottom corners.
 *
 * @param key Key used to identify the group in the lazy list. If null, no key is used.
 * @param modifier Modifier used to draw the group.
 * @param itemSpacing Gap between the individual cards.
 * @param shape Shape of the individual cards. If null, `MaterialTheme.shapes.medium` is used.
 * @param cardColor Card background color. If null, [CardDefaults.cardColors] is used.
 * @param outerPadding Clearance between the group and its container. If null,
 * `PreferenceTheme.horizontalSpacing` is used on all sides.
 * @param content Content of the group. Use [PreferenceCardGroupScope.card] to add items.
 */
public fun LazyListScope.preferenceCardGroup(
    key: String? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    itemSpacing: Dp = 4.dp,
    shape: Shape? = null,
    cardColor: Color? = null,
    outerPadding: PaddingValues? = null,
    content: @Composable PreferenceCardGroupScope.() -> Unit,
) {
    if (key != null) {
        item(key = key, contentType = "PreferenceCardGroup") {
            PreferenceCardGroup(
                modifier = modifier.then(highlightedKeyModifier(key)),
                itemSpacing = itemSpacing,
                shape = shape,
                cardColor = cardColor,
                outerPadding = outerPadding,
                content = content,
            )
        }
    } else {
        item(contentType = "PreferenceCardGroup") {
            PreferenceCardGroup(
                modifier = modifier,
                itemSpacing = itemSpacing,
                shape = shape,
                cardColor = cardColor,
                outerPadding = outerPadding,
                content = content,
            )
        }
    }
}
