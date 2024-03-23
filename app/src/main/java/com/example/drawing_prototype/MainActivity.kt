package com.example.drawing_prototype

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
    lateinit var drawingBoardModel: DrawingBoardModel


        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()

            //val drawingBoardModel: DrawingBoardModel by viewModels {
            //    DrawingBoardViewModelFactory((application as DrawingBoardApplication).DrawingBoardRepository)
            //}


            val factory = DrawingBoardViewModelFactory((application as DrawingBoardApplication).DrawingBoardRepository)
            drawingBoardModel = ViewModelProvider(this, factory).get(DrawingBoardModel::class.java)

            setContentView(R.layout.activity_main)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            /*
            val binding = ActivityMainBinding.inflate(layoutInflater)
            val child = binding.fragmentContainerView.getFragment<Fragment>()



            when (child) {
                is StartScreenFragment -> child.setStartButtonFunction {
                    val drawFragment = DrawingBoardFragment()
                    val transaction = this.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, drawFragment, "draw_tag")
                    transaction.addToBackStack(null)
                    transaction.commit()
                }

                is DrawingBoardFragment -> {
                    // Handle DrawingBoardFragment specific logic if needed
                }
                // Add more cases as necessary for other fragments
            }

            setContentView(binding.root)

            if(drawingBoardModel.isInitialize() == 0){
                drawingBoardModel.initializeModel(1100, 1100)
            }

             */
        }

}