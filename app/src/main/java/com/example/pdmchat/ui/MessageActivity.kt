package com.example.pdmchat.ui

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pdmchat.controller.SendMessageRtDbFrController
import com.example.pdmchat.databinding.MessageActivityBinding
import com.example.pdmchat.model.Constant.SENDER
import com.example.pdmchat.model.Message
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MessageActivity : AppCompatActivity() {
    private lateinit var sendMessageBinding: MessageActivityBinding
    private val messageController: SendMessageRtDbFrController by lazy {
        SendMessageRtDbFrController()
    }
    private lateinit var receiverEt: AutoCompleteTextView
    private lateinit var receiversAdapter: ArrayAdapter<String>
    private lateinit var receiversList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendMessageBinding = MessageActivityBinding.inflate(layoutInflater)
        setContentView(sendMessageBinding.root)

        receiverEt = sendMessageBinding.receiverEt
        val contentEt: EditText = sendMessageBinding.messageEt
        val sendBtn: Button = sendMessageBinding.sendBtn

        receiversList = mutableListOf()

        val sharedPreferences = getSharedPreferences("Receivers", Context.MODE_PRIVATE)
        val receiversSet = sharedPreferences.getStringSet("receivers", emptySet())
        receiversList.addAll(receiversSet ?: emptySet())

        receiversAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            receiversList
        )
        receiverEt.setAdapter(receiversAdapter)

        val sender = intent.getStringExtra(SENDER)

        sendBtn.setOnClickListener {
            val receiver = receiverEt.text.toString()
            val content = contentEt.text.toString()

            if (content.isNotEmpty() && receiver.isNotEmpty()) {
                sendMessage(sender, receiver, content)

                if (!receiversList.contains(receiver)) {
                    receiversList.add(receiver)
                    receiversAdapter.notifyDataSetChanged()

                    val editor = sharedPreferences.edit()
                    editor.putStringSet("receivers", receiversList.toSet())
                    editor.apply()
                }
            } else {
                val message = if (content.isEmpty()) "Insert message" else "Insert receiver"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendMessage(sender: String?, receiver: String, content: String) {
        val text = content.take(150)
        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = dateFormat.format(cal.time)
        val time = timeFormat.format(cal.time)

        val message = Message(
            sender = sender.orEmpty(),
            receiver = receiver,
            date = date,
            time = time,
            message = text
        )

        messageController.sendMessage(message)
        Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
        finish()
    }
}
