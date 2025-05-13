package com.example.adopciones_adoptpet.ui.components.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.domain.useCase.SyncAndLoadUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class PetViewModel(
    private val useCase: SyncAndLoadUseCase
) : ViewModel() {

    private val _pets = MutableStateFlow<List<PetWithImagesAndBreeds>>(emptyList())
    val pets: StateFlow<List<PetWithImagesAndBreeds>> = _pets

    private var syncJob: Job? = null
    private var hasStarted = false

    companion object {
        private const val SYNC_INTERVAL = 600_000L
    }

    init {
        startSyncAndObserve()
    }

    suspend fun syncPets() {
        try {
            useCase.sync()
        } catch (e: Exception) {
            Log.e("Sync", "Error al sincronizar: ${e.localizedMessage}")
        }
    }

    private suspend fun observePets() {
        useCase.invoke().collect { petList ->
            _pets.value = petList
        }
    }

    fun startSyncAndObserve() {
        if (hasStarted) return
        hasStarted = true

        startSyncingPeriodically(SYNC_INTERVAL)

        viewModelScope.launch {
            observePets()
        }
    }

    private fun startSyncingPeriodically(intervalMillis: Long) {
        syncJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                delay(intervalMillis)
                Log.d("Sync", "Sincronización iniciada")
                syncPets()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        syncJob?.cancel()
    }
}