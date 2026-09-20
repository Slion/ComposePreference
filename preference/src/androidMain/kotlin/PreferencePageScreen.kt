/*
 * Copyright 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.zhanghai.compose.preference

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * The adaptive two-pane settings screen: a list pane of [pages] with a search field, and a
 * detail pane showing the selected page's preferences.
 *
 * - Single-pane (e.g. a phone in portrait): selecting a page navigates to the detail pane;
 *   the top bar shows the page title with a back arrow, and system back pops the detail
 *   before dismissing the screen.
 * - Two-pane (e.g. wide windows): both panes are visible at once; the top bar shows the
 *   screen title and there is no navigation.
 *
 * The list pane shows an MD3-style search pill; while a query is entered, the page list is
 * replaced by the matching pages and preference entries, and selecting a result clears the
 * query and opens the page.
 *
 * @param title Title of the screen, shown in the top bar.
 * @param pages The pages to show in the list pane, in order.
 * @param modifier Modifier applied to the root surface.
 * @param onBack Called when the host should dismiss the screen (i.e. system back while the
 * list pane is on screen in a single-pane layout).
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
public fun PreferencePageScreen(
    title: String,
    pages: List<PreferencePage>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()
    val directive =
        remember(windowAdaptiveInfo) { calculatePaneScaffoldDirective(windowAdaptiveInfo) }
    val isTwoPane = directive.maxHorizontalPartitions >= 2
    val navigator =
        rememberListDetailPaneScaffoldNavigator<String>(
            scaffoldDirective = directive,
            adaptStrategies = ListDetailPaneScaffoldDefaults.adaptStrategies(),
            isDestinationHistoryAware = true,
        )
    val scope = rememberCoroutineScope()

    var selectedPageId by rememberSaveable { mutableStateOf<String?>(null) }
    var isOnDetailPane by rememberSaveable { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState()
    var query by remember { mutableStateOf("") }
    // Keep the query in sync with the input field's text.
    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }.collect { text ->
            if (query != text) query = text
        }
    }
    // The search field is the first focusable in the list pane, so the focus system restores
    // focus to it on launch and when returning from the detail pane, showing a brief focus
    // flash and keyboard. Keep it out of the focus tree during those transitions so focus is
    // never gained in the first place; it becomes focusable once the pane has settled.
    var fieldFocusEnabled by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(400)
        fieldFocusEnabled = true
    }
    val focusManager = LocalFocusManager.current
    var fieldFocused by remember { mutableStateOf(false) }

    fun clearQuery() {
        query = ""
        textFieldState.edit { replace(0, length, "") }
    }

    // The preference row that a search result navigated to is highlighted briefly, so the user
    // can find it in the opened page (mirrors the launcher settings behavior).
    var highlightedKey by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(highlightedKey) {
        if (highlightedKey != null) {
            delay(HIGHLIGHT_DURATION_MS)
            highlightedKey = null
        }
    }

    fun backAction() {
        if (!isTwoPane && isOnDetailPane) {
            isOnDetailPane = false
            scope.launch {
                // Keep the field out of the focus tree while the list pane is restored, so
                // the focus system does not put focus (and the keyboard) back on it.
                fieldFocusEnabled = false
                navigator.navigateBack()
                delay(400)
                fieldFocusEnabled = true
            }
        } else {
            onBack()
        }
    }

    // System back: the scaffold pops detail -> list first, then the host leaves the screen.
    BackHandler(onBack = ::backAction)

    fun selectPage(pageId: String) {
        selectedPageId = pageId
        isOnDetailPane = true
        scope.launch {
            navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail, contentKey = pageId)
        }
    }

    val isSearching = query.isNotEmpty()
    val matches = remember(pages, query) { searchPreferencePages(pages, query) }
    val searchEntries =
        remember(matches) {
            matches.flatMap { match ->
                buildList {
                    if (match.matches.isEmpty()) {
                        // The page itself matched (by title/summary): show the page row.
                        add(SearchEntry(match.page, key = null))
                    }
                    // Show one row per matching preference entry of the page's tree.
                    addAll(match.matches.map { SearchEntry(match.page, key = it.key) })
                }
            }
        }

    val currentPage = selectedPageId?.let { id -> pages.firstOrNull { it.id == id } }
    val visiblePage = if (isTwoPane || isOnDetailPane) currentPage else null
    val showBack = !isTwoPane && visiblePage != null
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text(text = visiblePage?.title ?: title) },
                navigationIcon = {
                    if (showBack) {
                        IconButton(onClick = ::backAction) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
            )
            ListDetailPaneScaffold(
                directive = navigator.scaffoldDirective,
                scaffoldState = navigator.scaffoldState,
                listPane = {
                    AnimatedPane {
                        Column(Modifier.fillMaxSize()) {
                            // An MD3-style search pill. The results are shown inline in this
                            // pane, so a plain text field is used instead of the state-based
                            // SearchBarDefaults.InputField, whose touch-mode focus coupling
                            // would force-expand the bar.
                            Surface(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .padding(horizontal = 8.dp),
                                shape = RoundedCornerShape(24.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                            ) {
                                Row(
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .padding(start = 8.dp, end = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Box(
                                        // A fixed-width tappable slot; the icon is centered in it,
                                        // so its edge lands 16.dp from the pill edge (8.dp row
                                        // padding + 8.dp within the slot).
                                        modifier =
                                            Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(20.dp))
                                                .then(
                                                    if (fieldFocused) {
                                                        Modifier.clickable {
                                                            focusManager.clearFocus(force = true)
                                                        }
                                                    } else {
                                                        Modifier
                                                    },
                                                ),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        if (fieldFocused) {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowBack,
                                                contentDescription = "Dismiss search",
                                                modifier = Modifier.size(24.dp),
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Filled.Search,
                                                contentDescription = null,
                                                modifier = Modifier.size(24.dp),
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            )
                                        }
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Box(Modifier.weight(1f)) {
                                        if (query.isEmpty()) {
                                            Text(
                                                text = "Search",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier =
                                                    Modifier.align(Alignment.CenterStart),
                                            )
                                        }
                                        BasicTextField(
                                            state = textFieldState,
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .then(
                                                        if (fieldFocusEnabled) {
                                                            Modifier
                                                        } else {
                                                            Modifier.focusProperties {
                                                                canFocus = false
                                                            }
                                                        },
                                                    )
                                                    .onFocusChanged { fieldFocused = it.isFocused },
                                            textStyle =
                                                MaterialTheme.typography.bodyLarge.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                ),
                                            cursorBrush =
                                                SolidColor(MaterialTheme.colorScheme.primary),
                                            lineLimits = TextFieldLineLimits.SingleLine,
                                        )
                                    }
                                    if (query.isNotEmpty()) {
                                        IconButton(onClick = ::clearQuery) {
                                            Icon(
                                                imageVector = Icons.Filled.Close,
                                                contentDescription = "Clear",
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            if (isSearching) {
                                LazyColumn(
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                                ) {
                                    items(searchEntries, key = { it.page.id + ":" + it.key }) { entry ->
                                        val matchedKey = entry.key
                                        SearchEntryRow(
                                            // An entry match shows the preference's own title
                                            // with the page as its subtitle; a page match
                                            // shows the page's title and summary.
                                            entry =
                                                matchedKey
                                                    ?.let { key ->
                                                        entry.page.searchEntries.firstOrNull { it.key == key }
                                                    },
                                            page = entry.page,
                                            onClick = {
                                                clearQuery()
                                                if (matchedKey != null) {
                                                    highlightedKey = matchedKey
                                                }
                                                selectPage(entry.page.id)
                                            },
                                        )
                                    }
                                    if (searchEntries.isEmpty()) {
                                        item {
                                            Text(
                                                text = "No matching pages",
                                                modifier = Modifier.padding(16.dp),
                                            )
                                        }
                                    }
                                }
                            } else {
                                LazyColumn(
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                                ) {
                                    items(pages, key = { it.id }) { page ->
                                        PreferencePageRow(
                                            page = page,
                                            selected = page.id == selectedPageId,
                                            onClick = { selectPage(page.id) },
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                detailPane = {
                    AnimatedPane {
                        val page = currentPage
                        if (page != null) {
                            CompositionLocalProvider(
                                LocalHighlightedPreferenceKey provides highlightedKey,
                            ) {
                                LazyColumn(
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                                ) {
                                    page.content(this)
                                }
                            }
                        }
                    }
                },
            )
        }
    }
}

/** A search result row: a page, or a preference entry of one of its pages. */
private data class SearchEntry(val page: PreferencePage, val key: String?)

/**
 * One row of the list pane, styled with the library's preference theme.
 */
@Composable
private fun PreferencePageRow(
    page: PreferencePage,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Preference(
        title = { Text(text = page.title) },
        summary = page.summary?.let { summary -> { Text(text = summary) } },
        onClick = onClick,
        modifier =
            Modifier.fillMaxWidth().background(
                if (selected) {
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                } else {
                    Color.Transparent
                },
            ),
    )
}

/**
 * One search result row, styled as an MD3 [ListItem] (as in the material3 search bar samples):
 * a leading search icon, the entry's title, and a supporting line naming the page it lives in.
 * A page-level match (no [entry]) shows the page's own title and summary instead.
 */
@Composable
private fun SearchEntryRow(
    entry: PreferenceSearchEntry?,
    page: PreferencePage,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(text = entry?.title ?: page.title) },
        supportingContent = {
            val supporting = entry?.let { page.title } ?: page.summary
            if (!supporting.isNullOrEmpty()) {
                Text(text = supporting)
            }
        },
        leadingContent = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
    )
}

/** How long a search-selected preference row stays highlighted in the detail pane. */
private const val HIGHLIGHT_DURATION_MS = 2000L
