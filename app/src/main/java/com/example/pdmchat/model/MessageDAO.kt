package com.example.pdmchat.model

interface MessageDAO {
    fun createContact(message: Message): Int
    fun retrieveContacts(): MutableList<Message>
}