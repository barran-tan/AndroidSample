package com.barran.sample.compose.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainPage(entryList: List<Entry>) {
    TestEntryList(entryList)
}

@Composable
fun TestEntryList(entryList: List<Entry>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 8.dp)
        for (entry in entryList) {
            EntryItem(entry = entry, modifier = modifier)
        }
    }
}

@Composable
fun EntryItem(entry: Entry, modifier: Modifier) {
    Button(onClick = entry.onClick, modifier = modifier) {
        Text(text = entry.txt)
    }
}

class Entry(val txt: String, val onClick: () -> Unit)