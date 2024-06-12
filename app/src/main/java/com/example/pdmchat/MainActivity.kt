package com.example.pdmchat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pdmchat.adapter.MessageAdapter
import com.example.pdmchat.controller.MessageRtDbFbController
import com.example.pdmchat.databinding.ActivityMainBinding
import com.example.pdmchat.model.Message
import com.example.pdmchat.ui.MessageActivity

class MainActivity : AppCompatActivity() {
    companion object {
        const val GET_MESSAGE = 1
        const val GET_MESSAGE_INTERVAL = 2000L
    }

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val messageList: MutableList<Message> = mutableListOf()

    private val messageController: MessageRtDbFbController by lazy {
        MessageRtDbFbController(this)
    }

    private val messageAdapter: MessageAdapter by lazy {
        MessageAdapter(this, messageList)
    }

    private lateinit var usernameInput: EditText
    private lateinit var saveButton: Button
    private lateinit var messageLv: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        amb.toolbarIn.toolbar.apply {
            subtitle = this@MainActivity.javaClass.simpleName
            setSupportActionBar(this)
        }

        usernameInput = findViewById(R.id.usernameInput)
        saveButton = findViewById(R.id.saveButton)
        messageLv = findViewById(R.id.messagesLv)

        saveButton.setOnClickListener {
            saveUsername()
        }

        amb.messagesLv.adapter = messageAdapter

        uiUpdaterHandler.apply {
            sendMessageDelayed(
                android.os.Message.obtain().apply {
                    what = GET_MESSAGE
                },
                GET_MESSAGE_INTERVAL
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sendMessage -> {
                val intent = Intent(this, MessageActivity::class.java)
                intent.putExtra("receiver", usernameInput.text.toString().trim())
                startActivity(intent)
                true
            }
            R.id.closeApp -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveUsername() {
        val username = usernameInput.text.toString().trim()
        if (username.isNotEmpty()) {
            Toast.makeText(this, "Username salvo: $username", Toast.LENGTH_SHORT).show()
            messageLv.visibility = View.VISIBLE
            usernameInput.visibility = View.GONE
            saveButton.visibility = View.GONE
        } else {
            Toast.makeText(this, "Digite um username válido", Toast.LENGTH_SHORT).show()
        }
    }

    val uiUpdaterHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            Log.d("MainActivity", "Mensagem recebida: ${msg.what}")
            if (msg.what == GET_MESSAGE) {
                sendMessageDelayed(
                    android.os.Message.obtain().apply {
                        what = GET_MESSAGE
                    },
                    GET_MESSAGE_INTERVAL
                )
                postDelayed({
                    messageController.getMessages()
                }, 1000)
            } else {
                msg.data.getParcelableArrayList<Message>("CHAT_ARRAY")?.let { _messageArray ->
                    Log.d("MainActivity", "Mensagens recebidas: $_messageArray")
                    val username = usernameInput.text.toString().trim()
                    updateChatsList(_messageArray.toMutableList(), username)
                }
            }
        }
    }

    private var isFirstLoad = true
    private fun updateChatsList(chats: MutableList<Message>, username: String) {
        val filteredChats = chats.filter { it.reciever == username }.sortedByDescending { it.getDateTime() }

        if (isFirstLoad) {
            messageList.clear()
            isFirstLoad = false
        }

        if (messageList.isEmpty()) {
            messageList.addAll(filteredChats)
        } else {
            for (chat in filteredChats) {
                if (!messageList.contains(chat)) {
                    messageList.add(0, chat)
                }
            }
            messageList.retainAll(filteredChats)
        }

        messageAdapter.notifyDataSetChanged()
    }
}
