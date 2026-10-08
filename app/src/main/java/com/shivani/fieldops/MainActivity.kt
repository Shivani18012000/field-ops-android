package com.shivani.fieldops

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shivani.fieldops.ui.WorkOrderListFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WorkOrderListFragment())
                .commit()
        }
    }
}
