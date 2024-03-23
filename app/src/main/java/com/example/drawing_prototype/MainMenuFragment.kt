package com.example.drawing_prototype

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.drawing_prototype.databinding.FragmentMainMenuBinding

import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import java.io.File


/**
 * A simple [Fragment] subclass.
 * Use the [MainMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainMenuFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container:ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainMenuBinding.inflate(layoutInflater)
        binding.composeView1.setContent {
            MyComposable(Modifier.padding(16.dp)){
                findNavController().navigate(R.id.action_MainMenuFragment_to_drawingBoardFragment)
            }
        }
        return binding.root
    }
}

@Composable
fun MyComposable(modifier:Modifier = Modifier, viewModel: DrawingBoardModel = viewModel(
    viewModelStoreOwner = LocalContext.current.findActivity()
),
                 onClick: ()->Unit){
        val context = LocalContext.current

        Scaffold(floatingActionButton = {
            ExtendedFloatingActionButton(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            onClick = onClick){
                Text(color = MaterialTheme.colorScheme.onTertiaryContainer,
                    text ="+ New Drawingboard")

            }
         })
        {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
                Column() {
                    Card(modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)

                    ) {
                        Text(
                            text = "Main Menu",
                            style = TextStyle(color = Color.Black, fontSize = 16.sp),
                            modifier = Modifier.padding(16.dp),
                            //style = MaterialTheme.typography.headlineLarge
                        )
                    }

                    val list by viewModel.allDrawingBoard.collectAsState(listOf())
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (drawingB in list.asReversed()) {
                            item {
                                val fileName = drawingB.fileName
                                if (fileName.isNotEmpty()) {
                                    val filePath = "${context.filesDir}/$fileName.png"
                                    if (File(filePath).exists()) {
                                        val imgBitmap = BitmapFactory.decodeFile(File(filePath).absolutePath)
                                        val painter = rememberImagePainter(data = imgBitmap)
                                        Drawingboard(painter, fileName)
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }
}

/*
fun checkFilePath(file : String){
    if (file.isNotEmpty()) {
        val filePath = "${requireContext().filesDir}/$file.png"
        if (File(filePath).exists()) {
            val localBitmap = BitmapFactory.decodeFile(filePath)
        }
    }
}

 */

@Composable
fun SayHello(){
    Text("Hello!")
}


@Composable
fun Drawingboard(painter: Painter, fileName: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Box (modifier = Modifier
            .height(150.dp)
            .padding(7.dp)){
            Image(painter = painter, contentDescription = "Pikachu")

        }
        Text(
            text = fileName,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
            modifier = modifier.padding(16.dp)
        )
        //val uriHandler = LocalUriHandler.current
        Button(
            modifier = modifier.padding(8.dp),
            contentPadding = PaddingValues(top = 0.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),
            onClick = {
                // to do
                //uriHandler.openUri(fact.source_url)
            }) {
            Text(text = "open",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(0.dp),)
        }
    }
}

@Preview
@Composable
fun DrawingBoardPreview(){
    val painter = painterResource(id = R.drawable.pikachu)
    Drawingboard(painter,"pikachu")
}