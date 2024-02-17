package com.example.drawing_prototype

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.drawing_prototype.databinding.DrawingBoardBinding

// Fragment class for the drawing board
// This class set up the drawing board and listen interaction event from the user
// Created by Chengyu Yang, Jiahua Zhao, Yitong Lu
class DrawingBoardFragment : Fragment() {

    lateinit var drawingBoardModel: DrawingBoardModel
    lateinit var binding: DrawingBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = DrawingBoardBinding.inflate(inflater)
        // Initialize VM
        drawingBoardModel = ViewModelProvider(requireActivity()).get(DrawingBoardModel::class.java)
        // Initialize the drawing board with bitmap and painter from VM
        binding.drawingBoard.initializeDrawingBoard(drawingBoardModel.bitmap.value!!, drawingBoardModel.paint)


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup user interact handlers
        setupTouchHandler()
        setupTextChangeWatcher()
        setupColorAndShapeSelectors()

        drawingBoardModel.bitmap.observe(viewLifecycleOwner, Observer {
            binding.drawingBoard.updateBitmap(it)
        })

    }

    // Set up listeners for changing painter color and shape
    fun setupColorAndShapeSelectors(){
        // color change click listener
        binding.black.setOnClickListener { drawingBoardModel.updatePaintColor(Color.parseColor("#000000"))}
        binding.blue.setOnClickListener { drawingBoardModel.updatePaintColor(Color.parseColor("#2196F3"))}
        binding.green.setOnClickListener { drawingBoardModel.updatePaintColor(Color.parseColor("#4CAF50"))}
        binding.yellow.setOnClickListener { drawingBoardModel.updatePaintColor(Color.parseColor("#FFEB3B"))}
        binding.redFirst.setOnClickListener { drawingBoardModel.updatePaintColor(Color.parseColor("#F44336"))}
        binding.pink.setOnClickListener { drawingBoardModel.updatePaintColor(Color.parseColor("#E91E63"))}
        binding.purple.setOnClickListener { drawingBoardModel.updatePaintColor(Color.parseColor("#673AB7"))}
        binding.orangeFirst.setOnClickListener { drawingBoardModel.updatePaintColor(Color.parseColor("#FF5722"))}
        binding.orangeSecond.setOnClickListener { drawingBoardModel.updatePaintColor(Color.parseColor("#FF9800"))}
        binding.cyan.setOnClickListener { drawingBoardModel.updatePaintColor(Color.parseColor("#14CCB7"))}
        binding.redSecond.setOnClickListener { drawingBoardModel.updatePaintColor(Color.parseColor("#8C1E1E"))}
        binding.violet.setOnClickListener { drawingBoardModel.updatePaintColor(Color.parseColor("#AC22A5"))}

        // paint shape change click listener
        binding.circle.setOnClickListener { drawingBoardModel.updateType("circle") }
        binding.rectangle.setOnClickListener { drawingBoardModel.updateType("rectangle") }
    }

    // Setup touch handler. Draw on the drawing board it user interact location
    fun setupTouchHandler(){
        view?.setOnTouchListener { v, event ->

            val x = event.x
            val y = event.y

            when(event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    v.performClick()
                    drawingBoardModel.draw(x, y)
                    true
                }
                else -> false
            }
        }
    }

    // Sets up a listener for change the pen size
    fun setupTextChangeWatcher(){
        binding.penSizeBox.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            // update the pen size in VM
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.toFloatOrNull().let {
                    if (it != null) {
                        drawingBoardModel.updateSizeOfPaint(it)
                    }
                }
            }

        })
    }
}