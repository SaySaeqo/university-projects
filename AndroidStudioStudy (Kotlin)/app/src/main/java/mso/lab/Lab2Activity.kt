package mso.lab

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

class Lab2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab2)

        val drawingActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                data?.let{
                    val bitmap = BitmapFactory.decodeFile(it.getStringExtra("path"))
                    findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
                }
            }
        }

        findViewById<Button>(R.id.drawing).setOnClickListener {
            drawingActivityLauncher.launch(Intent(this, DrawingActivity::class.java))
        }
    }
}