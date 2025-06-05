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
import com.example.adopciones_adoptpet.domain.useCase.AddPetUseCase
import com.example.adopciones_adoptpet.domain.useCase.GetBreedsByTypeUseCase
import com.example.adopciones_adoptpet.domain.useCase.SyncAndLoadUseCase
import com.example.adopciones_adoptpet.ui.constants.FilterKeys
import com.example.adopciones_adoptpet.ui.constants.PetMessages
import com.example.adopciones_adoptpet.utils.SessionManager
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
    private val getBreedsByTypeUseCase: GetBreedsByTypeUseCase,
    private val addPetUseCase: AddPetUseCase,
    private val sessionManager: SessionManager

) : ViewModel() {

    private val _breeds = mutableStateOf<List<BreedEntity>>(emptyList())
    val breeds: State<List<BreedEntity>> get() = _breeds

    private val _allPets = MutableStateFlow<List<PetWithImagesAndBreeds>>(emptyList())
    private val _filters = MutableStateFlow<Map<String, String>>(emptyMap())

    private val _insertResult = MutableStateFlow<Result<Unit>?>(null)
    val insertResult: StateFlow<Result<Unit>?> = _insertResult

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
            val session = sessionManager.getSession()
            if (session != null && session.role == "shelter") {
                useCase.sync(session.uid)
            } else {
                useCase.sync()
            }
        } catch (e: Exception) {
            Log.e(PetMessages.LOG_TAG_SYNC, "${PetMessages.SYNC_ERROR}: ${e.localizedMessage}")
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

        startSyncingPeriodically()

        viewModelScope.launch {
            observePets()
        }
    }

    private fun startSyncingPeriodically() {
        syncJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                delay(SYNC_INTERVAL)
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
                    FilterKeys.TYPE -> value == FilterKeys.All || pet.petType.displayName.equals(value, ignoreCase = true)
                    FilterKeys.SIZE -> value == FilterKeys.All  || pet.size.displayName.equals(value, ignoreCase = true)
                    FilterKeys.AGE -> {
                        val petAgeRange = PetAgeRange.entries.find { it.label == value }
                        petAgeRange?.matches(pet.age) ?: true
                    }

                    FilterKeys.GENDER -> value == FilterKeys.All  || pet.gender.displayName.equals(value, ignoreCase = true)
                    FilterKeys.BREED -> value == FilterKeys.All  || pet.breedName == value
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

    fun insertPet(pet: PetWithImagesAndBreeds) {

        viewModelScope.launch(Dispatchers.IO) {
            val shelterId = sessionManager.getSession()!!.uid
            _insertResult.value = addPetUseCase(pet, shelterId)
            Log.d(PetMessages.LOG_TAG_INSERT_PET, _insertResult.toString())

        }
    }

    fun clearInsertResult() {
        _insertResult.value = null
    }
}