package com.example.drawing_prototype

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

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

//    init {
//         System.loadLibrary("drawing_prototype")
//    }
//
//    private external fun invertColors(bitmap: Bitmap)
//    private external fun CW_90Degree(bitmap: Bitmap): Bitmap

    lateinit var drawingBoardModel: DrawingBoardModel

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()

            installSplashScreen()

            //requestWindowFeature(Window.FEATURE_NO_TITLE);

            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
            )


            val factory = DrawingBoardViewModelFactory((application as DrawingBoardApplication).DrawingBoardRepository)
            drawingBoardModel = ViewModelProvider(this, factory).get(DrawingBoardModel::class.java)

            setContentView(R.layout.activity_main)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

        }

}