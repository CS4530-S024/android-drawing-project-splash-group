package com.example.drawing_prototype

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.drawing_prototype.databinding.ActivityMainBinding

// MainActivity for the drawing board application
// Created by Chengyu Yang, Jiahua Zhao, Yitong Lu
class MainActivity : AppCompatActivity() {

    val drawingBoardModel: DrawingBoardModel by viewModels {
        DrawingBoardViewModelFactory(application)
    }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
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
        }

}