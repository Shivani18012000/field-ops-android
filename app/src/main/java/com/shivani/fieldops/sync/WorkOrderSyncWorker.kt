package com.shivani.fieldops.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shivani.fieldops.FieldOpsApplication

class WorkOrderSyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = try {
        (applicationContext as FieldOpsApplication).container.repository.refresh()
        Result.success()
    } catch (error: Exception) {
        if (runAttemptCount < 3) Result.retry() else Result.failure()
    }

    companion object { const val WORK_NAME = "field-work-order-sync" }
}
