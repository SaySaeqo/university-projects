package mso.lab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Lab1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab1)

        val textEdit: TextInputEditText = findViewById(R.id.te)
        val seekBar: SeekBar = findViewById(R.id.sb)
        val textView: TextView = findViewById(R.id.tv)

        textEdit.doAfterTextChanged { editable: Editable? ->
            val value  = editable.toString().takeUnless { it.isEmpty() }?.toIntOrNull() ?: 0
            seekBar.progress = value
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, fromUser: Boolean) {
                if (fromUser)
                    textEdit.setText("$p1")
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        var isWorking:Boolean = false
        var job: kotlinx.coroutines.Job? = null

        findViewById<Button>(R.id.start).setOnClickListener(View.OnClickListener { view ->
            if (isWorking) {
                return@OnClickListener
            }
            job = CoroutineScope(Dispatchers.Default).launch {
                isWorking = true
                var counter = textEdit.text.toString().takeUnless { it.isEmpty() }?.toIntOrNull() ?: 0
                while (isWorking && counter > 0) {
                    delay(100)
                    counter -= 1
                    withContext(Dispatchers.Main) {
                        seekBar.progress = counter
                        textEdit.setText(counter.toString())
                        textView.text = counter.toString()
                    }
                }
                isWorking = false
            }
        })
        findViewById<Button>(R.id.stop).setOnClickListener(View.OnClickListener { view ->
            isWorking = false; job?.cancel()})

    }
}