package com.example.myapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.myapplication.domain.model.MovieData
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: MovieViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        HandleSideEffect()
                        MainComposable(state, {
                            viewModel.onHandleIntent(it)
                        })
                    }
                }
            }
        }
    }

    @Composable
    private fun HandleSideEffect(){
        LaunchedEffect(Unit) {
            viewModel.sideEffect.collect {
                if (it is SideEffect.ShowError) {

                }
            }
        }
    }
}

@Composable
fun MainComposable(state: State, onHandleIntent: (UserIntent) -> Unit) {
    Column() {
        TextField(value = state.query, onValueChange = {
            onHandleIntent(UserIntent.UpdateQuery(it))
        })

        LazyColumn {
            itemsIndexed(state.movies){ index, item->
                MovieItem(item)
            }
            if(state.isLoading){
//                item {
//                    Circu
//                }
            }
        }
    }

}

@Composable
fun MovieItem(movieData: MovieData){
    Row() {
        AsyncImage(
            model = "https://api.themoviedb.org/" + movieData.poster_path,
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        Column {
            Text(text = movieData.title)
            Text(text = movieData.vote_average.toString())
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MyApplicationTheme {
//        Greeting("Android")
//    }
//}