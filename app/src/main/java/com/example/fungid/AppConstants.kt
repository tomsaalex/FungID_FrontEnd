package com.example.fungid

import java.time.format.DateTimeFormatter

object AppConstants {
    private const val NETWORK_TRANSFER_DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    val NETWORK_TRANSFER_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(NETWORK_TRANSFER_DATE_FORMAT)

    private const val DISPLAY_DATE_FORMAT = "yyyy-MM-dd"
    val DISPLAY_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DISPLAY_DATE_FORMAT)

    private const val DB_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    val DB_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DB_DATE_TIME_FORMAT)

    const val IMAGE_MEDIA_TYPE = "image/jpeg"
}