package com.example.pdmchat

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.pdmchat.databinding.ActivityMessageBinding

class MessageActivity: AppCompatActivity() {

    private val amb: ActivityMessageBinding by lazy {
        ActivityMessageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        amb.toolbarIn.toolbar.apply {
            subtitle = this@MessageActivity.javaClass.simpleName
            setSupportActionBar(this)
        }
    }
}