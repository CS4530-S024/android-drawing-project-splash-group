package com.example.drawing_prototype

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.drawing_prototype.databinding.ActivityMainBinding

// MainActivity for the drawing board application
// Created by Chengyu Yang, Jiahua Zhao, Yitong Lu
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inflates the layout for main activity
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val child = binding.fragmentContainerView.getFragment<Fragment>()

        // initialize the drawing board VM
        val drawingBoardModel = ViewModelProvider(this).get(DrawingBoardModel::class.java)
        drawingBoardModel.initializeModel()

        when(child){
            // If the current fragment is start screen
            // Set Function for start button click
            is StartScreenFragment -> child.setStartButtonFunction {
                // replace the StartScreenFragment with DrawingBoardFragment to display the drawing board
                val drawFragment = DrawingBoardFragment()
                val transaction = this.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, drawFragment, "draw_tag")
                transaction.addToBackStack(null)
                transaction.commit()
            }
            is DrawingBoardFragment -> {}
        }

        setContentView(binding.root)

    }
}