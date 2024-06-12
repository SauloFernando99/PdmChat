package com.example.pdmchat.model

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MessageDaoRtDbFrImpl : MessageDao {

    companion object {
        private const val CHAT_LIST_ROOT_NODE = "message"
    }

    private val messageRtDbFbReference = Firebase.database.getReference(CHAT_LIST_ROOT_NODE)
    private var isFirstValueEvent = true
    private val messageList = mutableListOf<Message>()

    init {
        messageRtDbFbReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null && !messageList.contains(message)) {
                    messageList.add(message)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    val index = messageList.indexOfFirst { it.id == message.id }
                    if (index != -1) {
                        messageList[index] = message
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    messageList.remove(message)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })

        messageRtDbFbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isFirstValueEvent) {
                    isFirstValueEvent = false
                    val messages = snapshot.children.mapNotNull { it.getValue(Message::class.java) }
                    if (messages.isNotEmpty()) {
                        messageList.addAll(messages.filterNot { messageList.contains(it) })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun sendMessage(message: Message): Int {
        val newMessageRef = messageRtDbFbReference.push()
        val id = newMessageRef.key ?: return -1
        message.id = id
        newMessageRef.setValue(message)
        return 1
    }

    override fun listMessages(): MutableList<Message> {
        return messageList
    }
}

