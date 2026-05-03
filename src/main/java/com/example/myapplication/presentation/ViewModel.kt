package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.MovieData
import com.example.myapplication.domain.model.MovieResponse
import com.example.myapplication.domain.repository.Repository
import com.example.myapplication.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject


data class State(
    val isLoading: Boolean = false,
    val query: String = "",
    val movies: List<MovieData> = emptyList(),
    val errorMsg: String? = null
)

sealed class UserIntent{
    data class LoadSearchedMovies(val query: String): UserIntent()
    data class UpdateQuery(val query: String): UserIntent()
}

sealed class SideEffect{
    data object ShowError: SideEffect()
}

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: Repository):ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()


    private val queryFlow = MutableStateFlow("")
    init {
        observeSearch()
        getPopularMovies()
    }

    fun observeSearch() {
        viewModelScope.launch {
            queryFlow.debounce(300).filter { it.isNotBlank() }.distinctUntilChanged()
                .collectLatest {
                    onHandleIntent(UserIntent.LoadSearchedMovies(it))
                }
        }
    }

    fun onHandleIntent(intent: UserIntent){
        when(intent){
            is UserIntent.LoadSearchedMovies -> loadSearchedMovies()
            is UserIntent.UpdateQuery -> {
                queryFlow.value = intent.query
                viewModelScope.launch {
                        _state.update { it.copy(query = intent.query) }
                }
            }

        }
    }

    fun loadSearchedMovies() = viewModelScope.launch{
            _state.update{it.copy(isLoading = true)}
        val response: ApiResponse<MovieResponse> = repository.getSearchedMovies()
        if (response is ApiResponse.Success) {
            _state.update{it.copy(isLoading = false, movies = (response.data as MovieResponse).results)}
        }
    }

    fun getPopularMovies() = viewModelScope.launch{
        _state.update{it.copy(isLoading = true)}
        val response: ApiResponse<MovieResponse> = repository.getPopularMovies()
        if (response is ApiResponse.Success) {
            _state.update{it.copy(isLoading = false, movies = response.data.results)}
        }
        else{
            _state.update{it.copy(isLoading = false, errorMsg = "8374")}
            _sideEffect.emit(SideEffect.ShowError)
        }
    }

}

