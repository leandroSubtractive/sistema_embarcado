package com.leandromendes.vehicleequalizer

import android.app.Application
import com.leandromendes.vehicleequalizer.data.AppDatabase
import com.leandromendes.vehicleequalizer.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Initializes important resources (database and repository) that need to be
 * available throughout the application's lifetime.
 */
class ProfileApplication : Application() {
    // Use SupervisorJob so that a coroutine failure does not affect others
    val applicationScope = CoroutineScope(SupervisorJob())

    // Lazy initialization of the database and repository
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { UserRepository(database.profileDao()) }
}