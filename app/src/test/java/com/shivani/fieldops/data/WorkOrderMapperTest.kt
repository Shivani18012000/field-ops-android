package com.shivani.fieldops.data

import com.shivani.fieldops.domain.Priority
import com.shivani.fieldops.domain.WorkOrderStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class WorkOrderMapperTest {
    @Test
    fun `unknown values use safe domain defaults`() {
        val entity = WorkOrderEntity("1", "Inspect", "Site", "URGENT", "QUEUED", "2026-10-08T10:00:00Z")
        val result = entity.toDomain()
        assertEquals(Priority.MEDIUM, result.priority)
        assertEquals(WorkOrderStatus.OPEN, result.status)
    }

    @Test
    fun `remote dto maps to cached entity`() {
        val dto = WorkOrderDto("2", "Replace device", "Clinic", "HIGH", "IN_PROGRESS", "2026-10-08T11:00:00Z")
        assertEquals(WorkOrderEntity("2", "Replace device", "Clinic", "HIGH", "IN_PROGRESS", "2026-10-08T11:00:00Z"), dto.toEntity())
    }
}
