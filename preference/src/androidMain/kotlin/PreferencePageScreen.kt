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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExpandedDockedSearchBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
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
 * The list pane shows an MD3 search bar: the collapsed [SearchBar] pill expands (via
 * [SearchBarState]) into the [ExpandedDockedSearchBar] docked search view, where the pages
 * matching the query are listed; selecting a result collapses the bar and opens the page.
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
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    var query by remember { mutableStateOf("") }
    // Keep the query in sync with the input field's text.
    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }.collect { text ->
            if (query != text) query = text
        }
    }
    // Clear the field whenever the search view collapses (e.g. a result was selected, the X
    // was pressed, or focus was lost).
    LaunchedEffect(searchBarState) {
        snapshotFlow { searchBarState.targetValue }.collect { value ->
            if (value == SearchBarValue.Collapsed && query.isNotEmpty()) {
                query = ""
                textFieldState.edit { replace(0, length, "") }
            }
        }
    }
    // The search field is the first focusable in the list pane, so it grabs initial focus on
    // launch and pops the keyboard. Clear it once after the field settles, but only if the bar
    // is still collapsed (i.e. the user hasn't started a search in the meantime).
    val focusManager = LocalFocusManager.current
    LaunchedEffect(focusManager) {
        delay(800)
        if (searchBarState.targetValue != SearchBarValue.Expanded) {
            focusManager.clearFocus(force = true)
        }
    }

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
            scope.launch { navigator.navigateBack() }
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
                                imageVector = Icons.ArrowBack,
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
                            // Shared input field for the collapsed and expanded search bar. In
                            // touch mode focus and expansion are coupled: focusing the field
                            // expands the bar, collapsing it clears the focus. The stock bar has
                            // no icons, so both slots are caller-supplied: the leading icon is a
                            // search icon when collapsed and a back arrow when expanded (as in
                            // the MD3 samples); a trailing clear button only appears while there
                            // is a query.
                            val inputField =
                                @Composable {
                                    SearchBarDefaults.InputField(
                                        textFieldState = textFieldState,
                                        searchBarState = searchBarState,
                                        // Live filtering as the query changes; the search
                                        // action itself is a no-op.
                                        onSearch = {},
                                        placeholder = { Text(text = "Search") },
                                        leadingIcon = {
                                            if (searchBarState.currentValue == SearchBarValue.Expanded) {
                                                IconButton(
                                                    onClick = {
                                                        scope.launch {
                                                            searchBarState.animateToCollapsed()
                                                        }
                                                    },
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.ArrowBack,
                                                        contentDescription = "Collapse search",
                                                    )
                                                }
                                            } else {
                                                Icon(
                                                    imageVector = Icons.Search,
                                                    contentDescription = null,
                                                )
                                            }
                                        },
                                        trailingIcon = {
                                            if (query.isNotEmpty()) {
                                                IconButton(onClick = ::clearQuery) {
                                                    Icon(
                                                        imageVector = Icons.Close,
                                                        contentDescription = "Clear",
                                                    )
                                                }
                                            }
                                        },
                                    )
                                }
                            SearchBar(
                                state = searchBarState,
                                inputField = inputField,
                                modifier = Modifier.padding(horizontal = 8.dp),
                            )
                            ExpandedDockedSearchBar(
                                state = searchBarState,
                                inputField = inputField,
                                content = {
                                    if (searchEntries.isEmpty()) {
                                        Text(
                                            text = "No matching pages",
                                            modifier = Modifier.padding(16.dp),
                                        )
                                    } else {
                                        searchEntries.forEach { entry ->
                                            val matchedKey = entry.key
                                            SearchEntryRow(
                                                // An entry match shows the preference's own
                                                // title with the page as its subtitle; a
                                                // page match shows the page's title and
                                                // summary.
                                                entry =
                                                    matchedKey
                                                        ?.let { key ->
                                                            entry.page.searchEntries.firstOrNull { it.key == key }
                                                        },
                                                page = entry.page,
                                                onClick = {
                                                    scope.launch {
                                                        searchBarState.animateToCollapsed()
                                                    }
                                                    if (matchedKey != null) {
                                                        highlightedKey = matchedKey
                                                    }
                                                    selectPage(entry.page.id)
                                                },
                                            )
                                        }
                                    }
                                },
                            )
                            Spacer(Modifier.height(8.dp))
                            LazyColumn(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                            ) {
                                items(pages, key = { it.id }) { page ->
                                    PreferencePageRow(
                                        page = page,
                                        selected = !isSearching && page.id == selectedPageId,
                                        onClick = { selectPage(page.id) },
                                    )
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
        leadingContent = { Icon(imageVector = Icons.Search, contentDescription = null) },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
    )
}

/** How long a search-selected preference row stays highlighted in the detail pane. */
private const val HIGHLIGHT_DURATION_MS = 2000L
