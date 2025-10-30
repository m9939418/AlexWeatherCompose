package com.alex.yang.weather.demo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.yang.weather.core.network.NetworkManager
import com.alex.yang.weather.domain.repository.ConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/10/16.
 *
 *
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    val networkManager: NetworkManager,
    private val configRepository: ConfigRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchConfig()
    }

    fun fetchConfig() {
        viewModelScope.launch {
            networkManager.guardOnline(
                doOnAvailable = {
                    _uiState.update { it.copy(showDialog = false, error = null) }

                    runCatching { configRepository.fetchAndCache() }
                        .fold(
                            onSuccess = {
                                delay(600L)

                                _uiState.update { it.copy(isReady = true, error = null) }
                            },
                            onFailure = { e ->
                                _uiState.update { it.copy(showDialog = true, error = "發生點問題" to "${e.message}") }

                                Log.e("DEBUG", "[DEBUG] fetchConfig Failure: ${e.message}")
                            }
                        )
                },
                doOnLost = {
                    _uiState.update { it.copy(isReady = true, showDialog = true) }
                }
            )
        }
    }

    data class UiState(
        val isReady: Boolean = false,
        val showDialog: Boolean = false,
        val error: ErrorPair? = null,
    )
}

typealias ErrorPair = Pair<String?, String?>
