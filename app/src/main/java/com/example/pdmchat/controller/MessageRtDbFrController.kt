package com.example.pdmchat.controller

import android.os.Message
import com.example.pdmchat.MainActivity
import com.example.pdmchat.model.Constant.MESSAGE_ARRAY
import com.example.pdmchat.model.MessageDao
import com.example.pdmchat.model.MessageDaoRtDbFrImpl


class MessageRtDbFrController(private val mainActivity: MainActivity) {
    private val messageDaoImpl: MessageDao = MessageDaoRtDbFrImpl()

    fun getChats() {
        val messageList = messageDaoImpl.listMessages()

        if (messageList.isNotEmpty()) {
            val sortedMessageList = messageList.sortedByDescending { it.getDateTime() }
            mainActivity.uiUpdaterHandler.sendMessage(
                Message.obtain().apply {
                    data.putParcelableArrayList(MESSAGE_ARRAY, ArrayList(sortedMessageList))
                }
            )
        }
    }
}
