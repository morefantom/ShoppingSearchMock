package com.prathamesh.shoppingsearchmock.presentation.search.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prathamesh.shoppingsearchmock.presentation.search.intent.SearchAction
import com.prathamesh.shoppingsearchmock.presentation.search.intent.SearchState

@Composable
fun SearchScreen(
    state: SearchState,
    onAction: (SearchAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        TextField(
            value = state.searchQuery,
            onValueChange = { enteredChar ->
                onAction(SearchAction.SearchQueryChanged(enteredChar))
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        if (state.isSearching) {
            Spacer(modifier = Modifier.height(16.dp))

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(state.searchedList.size) { index ->
                    Text(
                        text = state.searchedList[index],
                        modifier = Modifier
                            .clickable {
                                onAction(SearchAction.SearchedItemClicked(state.searchedList[index]))
                            }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        state = SearchState(),
        onAction = {},
    )
}