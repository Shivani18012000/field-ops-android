package com.shivani.fieldops.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.shivani.fieldops.data.WorkOrderRepository
import com.shivani.fieldops.domain.WorkOrder
import com.shivani.fieldops.domain.WorkOrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class WorkOrderViewModel(private val repository: WorkOrderRepository) : ViewModel() {
    private val filter = MutableStateFlow<WorkOrderStatus?>(null)
    private val _syncing = MutableLiveData(false)
    private val _error = MutableLiveData<String?>(null)

    val syncing: LiveData<Boolean> = _syncing
    val error: LiveData<String?> = _error
    val workOrders: LiveData<List<WorkOrder>> = combine(repository.observeWorkOrders(), filter) { orders, status ->
        orders.filter { status == null || it.status == status }
    }.asLiveData()

    init { refresh() }

    fun setFilter(status: WorkOrderStatus?) { filter.value = status }

    fun refresh() = viewModelScope.launch {
        _syncing.value = true
        _error.value = null
        try {
            repository.refresh()
        } catch (error: Exception) {
            _error.value = error.message ?: "Sync failed. Showing cached work orders."
        } finally {
            _syncing.value = false
        }
    }
}
