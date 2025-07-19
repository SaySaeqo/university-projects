package mso.lab

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.drawToBitmap
import java.io.File

class DrawingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)
    }

    override fun onBackPressed() {
        val intent = Intent()
        val bitmap = findViewById<DrawingView>(R.id.drawingView).drawToBitmap()
        val path = File(filesDir, "image.png").write(bitmap, format = Bitmap.CompressFormat.PNG).absolutePath
        intent.putExtra("path", path)
        setResult(RESULT_OK,intent)
        finish()
    }

    private fun File.createFileAndDirs() = apply {
        parentFile?.mkdirs()
        createNewFile()
    }

    private fun File.write(
        bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 80
    ) = apply {
        createFileAndDirs()
        outputStream().use {
            bitmap.compress(format, quality, it)
            it.flush()
        }
    }
}