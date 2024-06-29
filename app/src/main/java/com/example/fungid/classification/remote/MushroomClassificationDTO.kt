package com.example.fungid.classification.remote

import java.time.LocalDate

class MushroomClassificationDTO(
    val mushroomInstanceId: Long,
    val classificationResult: String,
    val takenAt: LocalDate
)