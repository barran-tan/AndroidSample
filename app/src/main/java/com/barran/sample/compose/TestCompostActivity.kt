package com.barran.sample.compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class TestCompostActivity:AppCompatActivity() {

    private val entryList = mutableListOf<Entry>()

    init {
        entryList.add(Entry("gesture test") {
            startActivity(Intent(this, GestureTestActivity::class.java))
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { 
            MaterialTheme() {
                Surface(modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background){
                    TestEntryList()
                }
            }
        }
    }

    @Composable
    fun TestEntryList(){
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
    fun EntryItem(entry:Entry, modifier: Modifier){
        Button(onClick = entry.onClick, modifier = modifier) {
            Text(text = entry.txt)
        }
    }

    inner class Entry(val txt: String, val onClick: () -> Unit) {

    }
}