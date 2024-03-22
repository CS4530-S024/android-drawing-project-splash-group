package com.example.drawing_prototype

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.drawing_prototype.databinding.StartScreenBinding
import java.io.File

// This StartScreenFragment displays the start screen of the application
// Created by Chengyu Yang, Jiahua Zhao, Yitong Lu
class StartScreenFragment : Fragment()  {

    private lateinit var binding: StartScreenBinding
    private var startButtonListener : () -> Unit = {}
    val drawingBoardModel: DrawingBoardModel by activityViewModels {
        DrawingBoardViewModelFactory(requireActivity().application)
    }

    fun setStartButtonFunction(newFunc: () -> Unit) {
        startButtonListener = newFunc
    }

    // This function inflates the fragment layout and initializes the back button listener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = StartScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        drawingBoardModel.getAllPicture()?.observe(viewLifecycleOwner,
//            object : Observer<List<DrawingBoard>> {
//                override fun onChanged(value: List<DrawingBoard>) {
////                    val a = 1
//                }
//            })

        // Setup a click listener for the start button
        binding.startButton.setOnClickListener {
            drawingBoardModel.initializeModel(1100, 1100)
            startButtonListener()
        }

        binding.openDrawingBroadButton.setOnClickListener {
            val fileName = binding.fileNameEditText.text.toString()
            if (fileName.isNotEmpty()) {
                val filePath = "${requireContext().filesDir}/$fileName.png"
                if (File(filePath).exists()) {
                    val localBitmap = BitmapFactory.decodeFile(filePath)
                    drawingBoardModel.initializeModel(1100, 1100)
                    drawingBoardModel.drawBitmap(localBitmap)
                    startButtonListener()
                }
                else{
                    Toast.makeText(context, "Your input file name is not exist", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}