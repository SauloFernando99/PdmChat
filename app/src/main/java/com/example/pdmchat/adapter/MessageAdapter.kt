package com.example.pdmchat.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.pdmchat.model.Message
import com.example.pdmchat.R

class MessageAdapter(context: Context, private val messageList: MutableList<Message>) :
    ArrayAdapter<Message>(context, R.layout.tile_message, messageList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val message = messageList[position]
        val chatTileView = convertView ?: LayoutInflater.from(context).inflate(R.layout.tile_message, parent, false)
        val holder = convertView?.tag as? TileContactHolder ?: TileContactHolder(
            chatTileView.findViewById(R.id.nameTv),
            chatTileView.findViewById(R.id.messageTv),
            chatTileView.findViewById(R.id.dateTimeTv)
        )

        holder.apply {
            nameTv.text = message.sender
            messageTv.text = message.message
            dateTimeTv.text = "${message.date} ${message.time}"
        }

        chatTileView.tag = holder
        return chatTileView
    }

    private data class TileContactHolder(
        val nameTv: TextView,
        val messageTv: TextView,
        val dateTimeTv: TextView
    )
}
