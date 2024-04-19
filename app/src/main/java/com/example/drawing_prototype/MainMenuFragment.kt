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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import java.io.File


/**
 * A simple [Fragment] subclass.
 * Use the [MainMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainMenuFragment : Fragment() {
    lateinit var drawingBoardModel: DrawingBoardModel
    override fun onCreateView(inflater: LayoutInflater, container:ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainMenuBinding.inflate(layoutInflater)
        val navController = findNavController()
        drawingBoardModel = ViewModelProvider(requireActivity()).get(DrawingBoardModel::class.java)
        binding.composeView1.setContent {
            MyComposable(navController, Modifier.padding(16.dp)){
                navController.navigate(R.id.action_MainMenuFragment_to_drawingBoardFragment)
            }
        }
        return binding.root
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MainMenuPreview() {
//
//
//
//}

@Composable
fun MyComposable(navController: NavController, modifier:Modifier = Modifier, viewModel: DrawingBoardModel = viewModel(
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
                viewModel.initializeModel(1100,1100)
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
                            style = TextStyle(color = Color.Black, fontSize = 20.sp),
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
                                        Drawingboard(painter, fileName, viewModel, navController)
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }
}


@Composable
fun SayHello(){
    Text("Hello!")
}


@Composable
fun Drawingboard(painter: Painter, fileName: String, vm : DrawingBoardModel, navController: NavController, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .fillMaxWidth()

    ) {
        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center){
            Image(
                painter = painterResource(id = R.drawable.cardbackgroundimage01),
                contentDescription = "Background Image",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            Row(modifier = Modifier.padding(all = 8.dp)) {
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .padding(7.dp)
                        .background(Color.LightGray)

                ) {
                    Box(modifier = Modifier
                        .height(150.dp)
                        .padding(6.dp)){
                            Image(painter = painter, contentDescription = "Pikachu")
                        }
                    //Image(painter = painter, contentDescription = "Pikachu")

                }
                Column(modifier = Modifier.padding(all = 8.dp)) {
                    Text(
                        text = fileName,
                        fontSize = 24.sp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                        modifier = modifier.padding(10.dp)
                    )
                    Row(modifier = Modifier.padding(all = 8.dp)) {
                    //val uriHandler = LocalUriHandler.current
                        Button(
                            modifier = modifier.padding(top = 30.dp, start = 10.dp, end = 5.dp, bottom = 10.dp).height(50.dp).width(75.dp),
                            contentPadding = PaddingValues(
                                top = 0.dp,
                                bottom = 0.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                            onClick = {
                                // to do
                                vm.openOldDrawingBoard(fileName)
                                navController.navigate(R.id.action_MainMenuFragment_to_drawingBoardFragment)

                            }) {
                            Text(
                                text = "open",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(0.dp),
                            )
                        }

                        Button(
                            modifier = modifier.padding(top = 30.dp, start = 5.dp, end = 10.dp, bottom = 10.dp).height(50.dp).width(75.dp),
                            contentPadding = PaddingValues(
                                top = 0.dp,
                                bottom = 0.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                            onClick = {
                                // to do
                                vm.deleteOldDrawingBoard(fileName)
                            }) {
                            Text(
                                text = "delete",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(0.dp),
                            )
                        }

                    }
                }
            }
        }
    }
}

