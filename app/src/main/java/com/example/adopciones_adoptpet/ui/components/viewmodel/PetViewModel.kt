package com.example.adopciones_adoptpet.ui.components.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.adopciones_adoptpet.domain.repository.PetRepository
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import kotlinx.coroutines.launch


class PetViewModel(
    private val repository: PetRepository
) : ViewModel() {

    private val _pets = mutableStateOf<List<PetWithImagesAndBreeds>>(emptyList())
    val pets: State<List<PetWithImagesAndBreeds>> = _pets

    init {
        viewModelScope.launch {
            repository.syncPetsWithRemote()
            loadPets()
        }

    }

    private fun loadPets() {
        viewModelScope.launch {
            val petsWithImages = repository.getPetsWithImagesAndBreeds()

            _pets.value = petsWithImages
        }
    }
}