package com.example.pdmchat.model

interface MessageDao {
    fun sendMessage(message: Message): Int
    fun listMessages(): MutableList<Message>
}