package com.shivani.fieldops

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.shivani.fieldops.data.FieldOpsContainer
import com.shivani.fieldops.sync.WorkOrderSyncWorker
import java.util.concurrent.TimeUnit

class FieldOpsApplication : Application() {
    val container by lazy { FieldOpsContainer(this) }

    override fun onCreate() {
        super.onCreate()
        val request = PeriodicWorkRequestBuilder<WorkOrderSyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WorkOrderSyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }
}
