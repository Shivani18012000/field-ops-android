package com.shivani.fieldops.data

import android.content.Context
import com.shivani.fieldops.domain.Priority
import com.shivani.fieldops.domain.WorkOrder
import com.shivani.fieldops.domain.WorkOrderStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface WorkOrderRepository {
    fun observeWorkOrders(): Flow<List<WorkOrder>>
    suspend fun refresh()
}

class OfflineFirstWorkOrderRepository(
    private val dao: WorkOrderDao,
    private val api: WorkOrderApi,
) : WorkOrderRepository {
    override fun observeWorkOrders(): Flow<List<WorkOrder>> =
        dao.observeAll().map { list -> list.map(WorkOrderEntity::toDomain) }

    override suspend fun refresh() {
        dao.upsertAll(api.getWorkOrders().map(WorkOrderDto::toEntity))
    }
}

fun WorkOrderEntity.toDomain() = WorkOrder(
    id,
    title,
    site,
    Priority.entries.firstOrNull { it.name == priority } ?: Priority.MEDIUM,
    WorkOrderStatus.entries.firstOrNull { it.name == status } ?: WorkOrderStatus.OPEN,
    updatedAt,
)

fun WorkOrderDto.toEntity() = WorkOrderEntity(id, title, site, priority, status, updatedAt)

class FieldOpsContainer(context: Context) {
    private val database = FieldOpsDatabase.create(context)
    val repository: WorkOrderRepository = OfflineFirstWorkOrderRepository(
        database.workOrderDao(),
        createWorkOrderApi(),
    )
}
