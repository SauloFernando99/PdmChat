package com.example.pdmchat.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
@Entity
data class Message(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = -1,
    @NonNull
    var sender: String = "",
    @NonNull
    var reciever: String = "",
    @NonNull
    var text: String = "",
    @NonNull
    var date: String = "",
    @NonNull
    var hour: String = "",
): Parcelable{
    fun getDateTime(): Date? {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return try {
            format.parse("$date $hour")
        } catch (e: Exception) {
            null
        }
    }
}
