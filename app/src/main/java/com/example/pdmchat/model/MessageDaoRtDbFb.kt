package com.example.pdmchat.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MessageDaoRtDbFb: MessageDAO {
    companion object {
        private const val CONTACT_LIST_ROOT_NODE = "message"
    }

    private val messageRtDbFbReference = Firebase.database.getReference(
        CONTACT_LIST_ROOT_NODE
    )

    private val messageList = mutableListOf<Message>()
    init {
        messageRtDbFbReference.addChildEventListener(
            object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val contact = snapshot.getValue<Message>()
                    if (contact != null) {
                        messageList.add(contact)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val contact = snapshot.getValue<Message>()

                    if (contact != null) {
                        val index = messageList.indexOfFirst { it.id == contact.id }
                        if (index != -1) {
                            messageList[index] = contact
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val contact = snapshot.getValue<Message>()

                    if (contact != null) {
                        val index = messageList.indexOfFirst { it.id == contact.id }
                        if (index != -1) {
                            messageList.removeAt(index)
                        }
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // NSA
                }

                override fun onCancelled(error: DatabaseError) {
                    // NSA
                }
            }
        )

        messageRtDbFbReference.addListenerForSingleValueEvent(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageMap = snapshot.getValue<Map<String, Message>>()

                    if (messageMap != null) {
                        messageList.clear()
                        messageList.addAll(messageMap.values)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // NSA
                }
            }
        )
    }

    override fun createMessage(message: Message): Int {
        createOrUpdateMessage(message)
        return 1
    }

    override fun retrieveMessages(): MutableList<Message> {
        return messageList
    }

    private fun createOrUpdateMessage(message: Message) {
        messageRtDbFbReference.child(message.id.toString()).setValue(message)
    }
}