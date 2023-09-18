package com.example.scraping_html

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UIスレッドのHandlerを作成
        handler = Handler(Looper.getMainLooper())

        // バックグラウンドスレッドでWebページを取得し、タイトル文字をリストで取得
        Thread {
            val url = "https://zenn.dev/topics/android"
            try {
                val conn = URL(url).openConnection()
                val reader = BufferedReader(InputStreamReader(conn.getInputStream()))
                var line: String?
                val content = StringBuilder()

                while (reader.readLine().also { line = it } != null) {
                    content.append(line)
                }

                reader.close()

                // テキストを取得したらUIスレッドで表示
                runOnUiThread {
                    val textView = findViewById<TextView>(R.id.textView)
                    textView.text = content.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()

    }
}