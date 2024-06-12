package com.example.pdmchat.model

interface MessageDAO {
    fun createMessage(message: Message): Int
    fun retrieveMessages(): MutableList<Message>
}