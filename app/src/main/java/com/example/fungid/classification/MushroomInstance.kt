package com.example.fungid.classification

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mushroom_instances")
data class MushroomInstance(
    @PrimaryKey val id: String = "",
    val mushroomSpecies: String = ""
)