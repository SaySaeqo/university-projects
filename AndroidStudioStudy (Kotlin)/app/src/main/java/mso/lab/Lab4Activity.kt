package mso.lab

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged

class Lab4Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab4)

        val inputArray = findViewById<android.widget.TextView>(R.id.inputArray)
        val outputArray = findViewById<android.widget.TextView>(R.id.outputArray)

        inputArray.doAfterTextChanged { text ->
            val array = text.toString().split(",").mapNotNull { it.toIntOrNull() }.toIntArray()
            if (array.isEmpty()) return@doAfterTextChanged
            val sorted = sortFromJNI(array)
            var sortedStr = ""
            for (num in sorted){
                sortedStr += "$num,"
            }
            outputArray.text = sortedStr
        }

        val myArray = intArrayOf(1,6,2,72,7,27,26,24,2,168,482,82,1)

        var myArrayStr = ""
        for (num in myArray){
            myArrayStr += "$num,"
        }
        inputArray.text = myArrayStr

        findViewById<TextView>(R.id.hello).text = stringFromJNI()

    }

    /**
     * A native method that is implemented by the 'lab4' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
    external fun sortFromJNI(intArray: IntArray): IntArray

    companion object {
        // Used to load the 'lab4' library on application startup.
        init {
            System.loadLibrary("lab4")
        }
    }
}