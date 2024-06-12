package com.example.pdmchat.model

import android.os.Parcelable
import androidx.annotation.NonNull
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
data class Message(
    @get:NonNull
    var id: String = "",
    @get:NonNull
    var sender: String = "",
    @get:NonNull
    var receiver: String = "",
    @get:NonNull
    var message: String = "",
    @get:NonNull
    var time: String = "",
    @get:NonNull
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