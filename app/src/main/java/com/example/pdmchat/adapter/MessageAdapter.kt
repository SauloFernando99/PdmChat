package com.example.pdmchat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.pdmchat.R
import com.example.pdmchat.model.Message

class MessageAdapter(context: Context, private val messageList: MutableList<Message>):
    ArrayAdapter<Message>(context, R.layout.tile_message, messageList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val message = messageList[position]

        var messageTileView = convertView
        if (messageTileView == null) {
            messageTileView = LayoutInflater.from(context).inflate(
                R.layout.tile_message,
                parent,
                false
            ).apply{
                tag = TileContactHolder(
                    findViewById(R.id.senderTv),
                    findViewById(R.id.messageTv),
                    findViewById(R.id.datetimeTv)
                )
            }
        }

        (messageTileView?.tag as TileContactHolder).apply{
            senderTv.text = message.sender
            messageTv.text = message.text
            messageTv.text = message.getDateTime().toString()
        }

        return messageTileView
    }

    private data class TileContactHolder(
        val senderTv: TextView, val messageTv: TextView, val datetimeTv: TextView
    )

}