package com.example.drawing_prototype

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.FirebaseStorage

//import com.example.drawing_prototype.databinding.ActivityMainBinding

// MainActivity for the drawing board application
// Created by Chengyu Yang, Jiahua Zhao, Yitong Lu

internal fun Context.findActivity(): ComponentActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

class MainActivity : AppCompatActivity() {

    lateinit var storage: FirebaseStorage

    lateinit var drawingBoardModel: DrawingBoardModel

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()

            installSplashScreen()

            storage = FirebaseStorage.getInstance()

            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
            )


            val factory = DrawingBoardViewModelFactory((application as DrawingBoardApplication).DrawingBoardRepository)
            drawingBoardModel = ViewModelProvider(this, factory).get(DrawingBoardModel::class.java)
            drawingBoardModel.getAllImageName()

            setContentView(R.layout.activity_main)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

        }

}