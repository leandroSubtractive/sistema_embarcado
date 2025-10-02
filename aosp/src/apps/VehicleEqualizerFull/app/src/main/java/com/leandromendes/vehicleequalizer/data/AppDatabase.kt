package com.leandromendes.vehicleequalizer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.leandromendes.vehicleequalizer.data.dao.ProfileDao
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Defines the list of entities (EqualizerProfile) and the database version (1)
@Database(entities = [EqualizerProfile::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Method to obtain the Data Access Object for EqualizerProfile
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope // Scope required for asynchronous database initialization
        ): AppDatabase {
            // Creates the database if INSTANCE is null (synchronized block)
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "equalizer_database"
                )
                    .fallbackToDestructiveMigration(false)
                    // Adds a callback to initialize the default profile
                    .addCallback(ProfileDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // Callback to populate the database the first time with the default profile
    private class ProfileDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    // Creates and inserts the default profile only once when creating the database
                    database.profileDao().insert(EqualizerProfile())
                }
            }
        }
    }
}