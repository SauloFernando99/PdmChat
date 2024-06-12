package com.example.pdmchat.controller

import com.example.pdmchat.model.Message
import com.example.pdmchat.model.MessageDao
import com.example.pdmchat.model.MessageDaoRtDbFrImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SendMessageRtDbFrController {
    private val messageDaoImpl: MessageDao = MessageDaoRtDbFrImpl()

    fun sendMessage(message: Message) {
        GlobalScope.launch {
            messageDaoImpl.sendMessage(message)
        }
    }
}