package com.example.adopciones_adoptpet.ui.components.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.example.adopciones_adoptpet.domain.model.BreedEntity
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.domain.model.enums.PetAgeRange
import com.example.adopciones_adoptpet.domain.model.enums.PetType
import com.example.adopciones_adoptpet.domain.useCase.GetBreedsByTypeUseCase
import com.example.adopciones_adoptpet.domain.useCase.SyncAndLoadUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class PetViewModel(
    private val useCase: SyncAndLoadUseCase,
    private val getBreedsByTypeUseCase: GetBreedsByTypeUseCase
) : ViewModel() {

    private val _breeds = mutableStateOf<List<BreedEntity>>(emptyList())
    val breeds: State<List<BreedEntity>> get() = _breeds

    private val _allPets = MutableStateFlow<List<PetWithImagesAndBreeds>>(emptyList())
    private val _filters = MutableStateFlow<Map<String, String>>(emptyMap())

    val pets: StateFlow<List<PetWithImagesAndBreeds>> = combine(
        _allPets,
        _filters
    ) { allPets, filters ->
        applyFilters(allPets, filters)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

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
            _allPets.value = petList
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

    fun applyFilters(filters: Map<String, String>) {
        _filters.value = filters
    }

    private fun applyFilters(
        allPets: List<PetWithImagesAndBreeds>,
        filters: Map<String, String>
    ): List<PetWithImagesAndBreeds> {
        return allPets.filter { pet ->
            filters.all { (key, value) ->
                when (key) {
                    "Tipo" -> value == "Todos" || pet.petType.displayName.equals(value, ignoreCase = true)
                    "Tamaño" -> value == "Todos" || pet.size.displayName.equals(value, ignoreCase = true)
                    "Edad" -> {
                        val PetAgeRange = PetAgeRange.values().find { it.label == value }
                        PetAgeRange?.matches(pet.age) ?: true
                    }

                    "Genero" -> value == "Todos" || pet.gender.displayName.equals(value, ignoreCase = true)
                    "Raza" -> value == "Sin razas disponibles" || pet.breedName == value
                    else -> true
                }
            }
        }
    }
    fun loadBreeds(type: PetType) {
        viewModelScope.launch {
            _breeds.value = getBreedsByTypeUseCase.invoke(type)
        }
    }
}