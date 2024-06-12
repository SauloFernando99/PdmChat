package com.example.pdmchat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pdmchat.adapter.MessageAdapter
import com.example.pdmchat.controller.MessageRtDbFrController
import com.example.pdmchat.databinding.ActivityMainBinding
import com.example.pdmchat.model.Constant.MESSAGE_ARRAY
import com.example.pdmchat.model.Constant.SENDER
import com.example.pdmchat.model.Message
import com.example.pdmchat.ui.MessageActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val GET_MESSAGE = 1
        const val GET_MESSAGES_INTERVAL = 2000L
    }

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val messageList: MutableList<Message> = mutableListOf()

    private val messageController: MessageRtDbFrController by lazy {
        MessageRtDbFrController(this)
    }

    private val messageAdapter: MessageAdapter by lazy {
        MessageAdapter(this, messageList)
    }

    private lateinit var usernameInput: EditText
    private lateinit var saveButton: View
    private lateinit var messageLv: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        amb.toolbarIn.toolbar.apply {
            subtitle = this@MainActivity.javaClass.simpleName
            setSupportActionBar(this)
        }

        usernameInput = findViewById(R.id.usernameEt)
        saveButton = findViewById(R.id.saveBtn)
        messageLv = findViewById(R.id.messageLv)

        uiUpdaterHandler.sendMessageDelayed(
            android.os.Message.obtain().apply { what = GET_MESSAGE },
            GET_MESSAGES_INTERVAL
        )

        amb.messageLv.adapter = messageAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.send_message -> {
                val intent = Intent(this, MessageActivity::class.java)
                intent.putExtra(SENDER, usernameInput.text.toString().trim())
                startActivity(intent)
                true
            }
            R.id.close_app -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun saveUsername(view: View) {
        val username = usernameInput.text.toString().trim()
        if (username.isNotEmpty()) {
            Toast.makeText(this, "Username saved: $username", Toast.LENGTH_SHORT).show()
            messageLv.visibility = View.VISIBLE
            usernameInput.visibility = View.GONE
            saveButton.visibility = View.GONE
        } else {
            Toast.makeText(this, "Insert a valid username", Toast.LENGTH_SHORT).show()
        }
    }

    val uiUpdaterHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: android.os.Message) {
            if (msg.what == GET_MESSAGE) {
                sendMessageDelayed(
                    android.os.Message.obtain().apply { what = GET_MESSAGE },
                    GET_MESSAGES_INTERVAL
                )
                postDelayed({
                    messageController.getMessages()
                }, 1000)
            } else {
                msg.data.getParcelableArrayList<Message>(MESSAGE_ARRAY)?.let { _messageArray ->
                    val username = usernameInput.text.toString().trim()
                    updateMessagesList(_messageArray.toMutableList(), username)
                }
            }
        }
    }

    private var isFirstLoad = true

    fun updateMessagesList(messages: MutableList<Message>, username: String) {
        val filteredMessages = messages.filter { it.receiver == username }
            .sortedByDescending { it.getDateTime() }

        if (isFirstLoad) {
            messageList.clear()
            isFirstLoad = false
        }

        if (messageList.isEmpty()) {
            messageList.addAll(filteredMessages)
        } else {
            filteredMessages.filterNot { messageList.contains(it) }
                .forEach { messageList.add(0, it) }
            messageList.retainAll(filteredMessages)
        }
        messageAdapter.notifyDataSetChanged()
    }
}

