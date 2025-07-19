package mso.lab

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.lab1).setOnClickListener{
            startActivity(Intent(this, Lab1Activity::class.java))
        }
        findViewById<Button>(R.id.lab2).setOnClickListener{
            startActivity(Intent(this, Lab2Activity::class.java))
        }
        findViewById<Button>(R.id.lab3).setOnClickListener{
            startActivity(Intent(this, Lab3Activity::class.java))
        }
        findViewById<Button>(R.id.lab4).setOnClickListener{
            startActivity(Intent(this, Lab4Activity::class.java))
        }

    }

    override fun onBackPressed() {
        val alert = AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Do you want to exit?")
            .setNegativeButton("No", DialogInterface.OnClickListener{ a, b -> } )
            .setPositiveButton("yes", DialogInterface.OnClickListener({a,b -> finish()}))
            .create()
        alert.show()
    }
}



