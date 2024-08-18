package com.example.fungid.data.classification

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "mushroom_instances")
data class MushroomInstance(
    @PrimaryKey val id: String = "",
    val mushroomSpecies: String = "",
    val sampleTakenAt: LocalDateTime = LocalDateTime.now(),
    var localImagePath: String = ""
)