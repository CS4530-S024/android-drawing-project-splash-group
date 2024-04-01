package com.example.drawing_prototype

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import coil.compose.rememberImagePainter
import com.example.drawing_prototype.databinding.StartScreenBinding
import java.io.File

// This StartScreenFragment displays the start screen of the application
// Created by Chengyu Yang, Jiahua Zhao, Yitong Lu
class StartScreenFragment : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = StartScreenBinding.inflate(layoutInflater)

        //ComposeView gives us a `Composable` context to run functions in
        binding.composeView3.setContent {
            MyStartScreen(Modifier.padding(16.dp)){
                findNavController().navigate(R.id.action_StartScreenFragment_to_MainMenuFragment)
            }
        }

        return binding.root
    }


}

@Composable
fun MyStartScreen(modifier: Modifier = Modifier,
                  viewModel: DrawingBoardModel = viewModel(
                     viewModelStoreOwner = LocalContext.current.findActivity()
                 ),
                  onClick: ()->Unit){

    SetBackgroundImageScreen()

    Column(modifier = modifier.padding(start = 45.dp, top = 600.dp, end = 45.dp, bottom = 16.dp)
    ) {
        //currentCount can be used like an int and when the flow/state
        //changes it will trigger recomposition of everything that
        /*
            Box(
                modifier = Modifier
                    .height(500.dp)
                    .padding(7.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.start), contentDescription = "Your Image Description")
            }

         */

        Button(
            modifier = modifier.padding(10.dp).height(50.dp).width(230.dp),
            contentPadding = PaddingValues(top = 0.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),
            onClick = onClick ){
                Text(text = "open",
                    fontSize = 30.sp,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(0.dp),)

        }

        //depends on currentCount
        //Button(onClick = onClick) {
        //    Text("start")
        //}
    }

}

@Composable
fun SetBackgroundImageScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.start1),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Adjust the scaling to suit your needs
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BackgroundImageScreenPreview() {
    SetBackgroundImageScreen()
}

    /*
    private lateinit var binding: StartScreenBinding
    //private var startButtonListener : () -> Unit = {}

    lateinit var drawingBoardModel: DrawingBoardModel

    //fun setStartButtonFunction(newFunc: () -> Unit) {
    //    startButtonListener = newFunc
    //}

    // This function inflates the fragment layout and initializes the back button listener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding.startButton.setOnClickListener {
            //startButtonListener()
            findNavController().navigate(R.id.action_composableFragment1_to_fragment2)
        }

        binding = StartScreenBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        drawingBoardModel.getAllPicture()?.observe(viewLifecycleOwner,
//            object : Observer<List<DrawingBoard>> {
//                override fun onChanged(value: List<DrawingBoard>) {
//                    val a = 1
//                }
//            })

        // Setup a click listener for the start button

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

     */
