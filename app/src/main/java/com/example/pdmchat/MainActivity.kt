package com.example.pdmchat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pdmchat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        amb.toolbarIn.toolbar.apply {
            subtitle = this@MainActivity.javaClass.simpleName
            setSupportActionBar(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sendMessage -> {
                val intent = Intent(this, MessageActivity::class.java)
                //intent.putExtra("remetente", usernameInput.text.toString().trim())
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
}