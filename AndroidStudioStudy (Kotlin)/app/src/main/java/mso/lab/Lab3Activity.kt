package mso.lab

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.textfield.TextInputEditText
import java.io.FileOutputStream
import androidx.core.net.toUri

class Lab3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab3)

        val textEdit = findViewById<TextInputEditText>(R.id.textEdit)
        val lastUri = findViewById<TextView>(R.id.lastUri)

        val openFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult
            val uri = result.data?.dataString?.toUri() ?: return@registerForActivityResult
            lastUri.text = getFileName(contentResolver, uri)
            contentResolver.openInputStream(uri)?.use {
                it.bufferedReader().use {
                    val tmp = it.readText()
                    Log.d("tralala", tmp)
                    textEdit.setText(tmp)
                }
            }
        }
        val saveFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult
            val uri = result.data?.dataString?.toUri() ?: return@registerForActivityResult
            val pfd = contentResolver.openFileDescriptor(uri, "w")
            FileOutputStream(pfd?.fileDescriptor).use {
                it.bufferedWriter().use {
                    Log.d("tralala", textEdit.text.toString())
                    it.write(textEdit.text.toString())
                    it.flush()
                }
            }
        }

        findViewById<Button>(R.id.openButton).setOnClickListener{
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, Environment.DIRECTORY_DOCUMENTS)
                type = "text/*"
            }
            openFileLauncher.launch(intent)
        }

        findViewById<Button>(R.id.saveButton).setOnClickListener{
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                putExtra(Intent.EXTRA_TITLE, "file.txt")
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, Environment.DIRECTORY_DOCUMENTS)
                type = "text/plain"
            }
            saveFileLauncher.launch(intent)
        }
    }

    fun getFileName(contentResolver: ContentResolver, uri: Uri): String? =
        try {
            contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                ?.use { cursor -> // .use is important and makes it more than a pure one-liner
                    if (cursor.moveToFirst()) cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)) else null
                }
        } catch (e: Exception) {
            // Log.e("GET_FILE_NAME", "Error: ${e.message}") // Proper logging is still good
            uri.lastPathSegment // Fallback, less reliable
        }
}