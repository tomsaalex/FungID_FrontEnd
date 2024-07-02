package com.example.fungid.classification.remote

import java.time.LocalDateTime

class MushroomClassificationDTO(
    val mushroomInstanceId: Long,
    val classificationResult: String,
    val takenAt: LocalDateTime
)