package com.shivani.fieldops.domain

data class WorkOrder(
    val id: String,
    val title: String,
    val site: String,
    val priority: Priority,
    val status: WorkOrderStatus,
    val updatedAt: String,
)

enum class Priority { LOW, MEDIUM, HIGH }
enum class WorkOrderStatus { OPEN, IN_PROGRESS, COMPLETE }
