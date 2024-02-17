package com.example.drawing_prototype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.drawing_prototype.databinding.StartScreenBinding

// This StartScreenFragment displays the start screen of the application
class StartScreenFragment : Fragment()  {

    private var startButtonListener : () -> Unit = {}

    fun setStartButtonFunction(newFunc: () -> Unit) {
        startButtonListener = newFunc
    }

    // This function inflates the fragment layout and initializes the back button listener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = StartScreenBinding.inflate(inflater, container, false)

        // Setup a click listener for the start button
        binding.startButton.setOnClickListener {
            startButtonListener()
        }
        return binding.root
    }
}