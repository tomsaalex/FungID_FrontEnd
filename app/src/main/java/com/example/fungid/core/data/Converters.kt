package com.example.fungid.core.data

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converters {

    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, AppConstants.DB_DATE_TIME_FORMATTER) }
    }

    @TypeConverter
    fun localDateTimeToString(localDateTime: LocalDateTime?): String? {
        return localDateTime?.format(AppConstants.DB_DATE_TIME_FORMATTER)
    }
}