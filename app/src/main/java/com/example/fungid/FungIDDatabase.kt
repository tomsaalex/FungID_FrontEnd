package com.example.fungid

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fungid.classification.MushroomInstance
import com.example.fungid.classification.local.MushroomInstanceDao

@Database(entities = arrayOf(MushroomInstance::class), version = 1)
@TypeConverters(Converters::class)
abstract class FungIDDatabase: RoomDatabase() {
    abstract fun mushroomInstanceDao(): MushroomInstanceDao

    companion object {
        @Volatile
        private var INSTANCE: FungIDDatabase? = null


        fun getDatabase(context: Context): FungIDDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    FungIDDatabase::class.java,
                    "fungid_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}