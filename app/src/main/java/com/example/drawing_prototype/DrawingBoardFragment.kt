package com.example.drawing_prototype

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.drawing_prototype.databinding.DrawingBoardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// Fragment class for the drawing board
// This class set up the drawing board and listen interaction event from the user
// Created by Chengyu Yang, Jiahua Zhao, Yitong Lu
class DrawingBoardFragment : Fragment() {

    val drawingBoardModel: DrawingBoardModel by activityViewModels {
        DrawingBoardViewModelFactory(requireActivity().application)
    }
    lateinit var binding: DrawingBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = DrawingBoardBinding.inflate(inflater)

        if(drawingBoardModel.isInitialize() == 0){
            drawingBoardModel.initializeModel(binding.drawingBoard.width, binding.drawingBoard.height)
        }

        // Initialize the drawing board with bitmap and painter from VM
        binding.drawingBoard.initializeDrawingBoard(drawingBoardModel.bitmap.value!!, drawingBoardModel.paint)

        return binding.root
    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup user interact handlers
        setupTextChangeWatcher()
        setupColorAndShapeSelectors()

        drawingBoardModel.bitmap.observe(viewLifecycleOwner, Observer {
            binding.drawingBoard.updateBitmap(it)
        })

        binding.saveBitmapButton.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_bitmap_name, null, false)
            val bitmapNameEditText = dialogView.findViewById<EditText>(R.id.bitmapNameEditText)
            MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .setPositiveButton("OK", object : OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val fileName = bitmapNameEditText.text.toString()
                        drawingBoardModel.saveCurrentBitmap(fileName)
                    }
                })
                .show()
        }
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