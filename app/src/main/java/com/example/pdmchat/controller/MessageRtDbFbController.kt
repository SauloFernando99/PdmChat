package com.example.pdmchat.controller

import android.util.Log
import com.example.pdmchat.MainActivity
import com.example.pdmchat.model.Message
import com.example.pdmchat.model.MessageDAO
import com.example.pdmchat.model.MessageDaoRtDbFb
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MessageRtDbFbController (private val mainActivity: MainActivity) {
    private val messageDaoImpl: MessageDAO = MessageDaoRtDbFb()

    fun insertMessage(message: Message) {
        GlobalScope.launch {
            messageDaoImpl.createMessage(message)
        }
        Log.d("Firebase", "Sending Message to firebase: $message")
    }
    fun getMessages(){
        val messageList = messageDaoImpl.retrieveMessages()
        if (messageList.isNotEmpty()) {
            mainActivity.uiUpdaterHandler.sendMessage(
                android.os.Message.obtain().apply{
                    data.putParcelableArrayList(
                        "MESSAGE_ARRAY",
                        ArrayList(messageList)
                    )
                }
            )
        }
    }

}