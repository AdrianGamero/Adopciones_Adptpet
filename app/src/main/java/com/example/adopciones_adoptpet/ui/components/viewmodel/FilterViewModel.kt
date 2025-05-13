package com.example.adopciones_adoptpet.ui.components.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adopciones_adoptpet.domain.model.Filter
import com.example.adopciones_adoptpet.domain.model.PetType
import com.example.adopciones_adoptpet.domain.useCase.GetFiltersUseCase
import kotlinx.coroutines.launch

class FilterViewModel(private val useCase: GetFiltersUseCase) : ViewModel() {
    private val _filters = mutableStateOf<List<Filter>>(emptyList())
    val filters: State<List<Filter>> = _filters

    private val _selectedFilters = mutableStateOf<Map<String, String>>(emptyMap())
    val selectedFilters: State<Map<String, String>> = _selectedFilters

    private val _showFilters = mutableStateOf(false)
    val showFilters: State<Boolean> = _showFilters

    private val _appliedFilters = mutableStateOf<Map<String, String>>(emptyMap())
    val appliedFilters: State<Map<String, String>> = _appliedFilters



    fun loadFilters(petType: PetType?) {
        viewModelScope.launch {
            val filtersResult = useCase.invoke(petType)
            _filters.value = filtersResult
        }
    }

    fun loadRequestFilters() {
        viewModelScope.launch {
            val filtersResult = useCase.invoke(null)
            _filters.value = filtersResult
        }
    }

    fun updateFilter(name: String, selected: String) {
        _selectedFilters.value = _selectedFilters.value + (name to selected)
    }

    fun toggleFilters() {
        _showFilters.value = !_showFilters.value
    }

    fun applyFilters(selected: Map<String, String>) {
        _appliedFilters.value = selected
        _showFilters.value = false
    }

    fun cancelFilters() {
        _showFilters.value = false
    }
}