package com.example.pdmchat.model

import android.os.Parcelable
import androidx.annotation.NonNull
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
data class Message(
    @NonNull
    var id: String = "",
    @NonNull
    var sender: String = "",
    @NonNull
    var receiver: String = "",
    @NonNull
    var message: String = "",
    @NonNull
    var time: String = "",
    @NonNull
    var date: String = ""
) : Parcelable{
    fun getDateTime(): Date? {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return try {
            format.parse("$date $time")
        } catch (e: Exception) {
            null
        }
    }
}