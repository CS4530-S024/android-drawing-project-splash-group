package com.example.drawing_prototype

import android.app.Application
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow


// VM contains function to setup the bitmap, store change the pen's size, color, and shape,
// And draw color on the drawing board touch location
// Created by Chengyu Yang, Jiahua Zhao, Yitong Lu
class DrawingBoardModel(private val repository: DrawingBoardRepository): ViewModel() {

    init {
         System.loadLibrary("drawing_prototype")
    }

    private external fun invertColors(bitmap: Bitmap)
    private external fun CW_90Degree(bitmap: Bitmap): Bitmap

    // Use example:
    //        bitmap.value?.let { invertColors(it) }
    //        bitmap.value = bitmap.value
    //        bitmap.value?.let {bitmap.value = CW_90Degree(it)}

    // MutableLiveData for observer changes on current bitmap
    //private var repository: DrawingBoardRepository? = null
    val allDrawingBoard : Flow<List<DrawingBoard>> = repository.allDrawingBoard

//    private external fun invertColors(bitmap: Bitmap)

    lateinit var bitmap: MutableLiveData<Bitmap>

    private var initialize: Int = 0
    // Initial pen
    val paint = Paint()
    val eraser_paint = Paint()
    var size_of_paint: Float = 20.0f
    var size_of_eraser: Float = 20.0f
    var paint_type = "circle"
    var filename = ""
    // Canvas for the bitmap, use for drawing
    private lateinit var bitmapCanvas: Canvas

    // Initialize the model, create a square bimap and set default value for pen color and size
    fun initializeModel(width : Int, height : Int){
        paint.color = Color.BLACK
        // Create a new square bitmap with width 1024
        bitmap = MutableLiveData(android.graphics.Bitmap.createBitmap(1100, 1100,
            android.graphics.Bitmap.Config.ARGB_8888))
        bitmapCanvas = Canvas(bitmap.value!!)
        // Set the background color to white
        bitmapCanvas.drawColor(Color.WHITE)
        size_of_paint = 20.0f
        initialize = 1
        filename = ""
    }


    fun InvertPixel(){
        bitmap.value?.let { invertColors(it) }
        bitmap.value = bitmap.value
    }

    fun CW_rotate(){
        bitmap.value?.let {bitmap.value = CW_90Degree(it)}
    }

    fun createBitmap(width: Int, height: Int, config: Bitmap.Config): Bitmap {
        return Bitmap.createBitmap(width, height, config)
    }

    fun initializeModel(width : Int, height : Int, fileName:String){
        paint.color = Color.BLACK
        // Create a new square bitmap with width 1024
        bitmap = MutableLiveData(android.graphics.Bitmap.createBitmap(1100, 1100,
            android.graphics.Bitmap.Config.ARGB_8888))
        bitmapCanvas = Canvas(bitmap.value!!)
        // Set the background color to white
        bitmapCanvas.drawColor(Color.WHITE)
        size_of_paint = 20.0f
        initialize = 1
        filename = fileName
    }

    fun drawBitmap(localBitmap: Bitmap) {
        val mutableBitmap = localBitmap.copy(Bitmap.Config.ARGB_8888, true)
        bitmap.value = mutableBitmap
        bitmapCanvas = Canvas(mutableBitmap)
    }

    // This will save the current bitmap
    fun saveCurrentBitmap(fileName: String) {
        bitmap.value?.let { bmp ->
            viewModelScope.launch {
                repository.storePicture(bmp, fileName)
            }
        }
    }

    fun openOldDrawingBoard(fileName: String){
        val Bitmap = repository.loadBitmap(fileName)
        initializeModel(1100, 1100, fileName)
        drawBitmap(Bitmap)
    }

    fun deleteOldDrawingBoard(fileName: String) {

        repository.removePicture(fileName)

    }

    // Update pen type
    fun updateType(string: String){
        paint_type = string
    }

    fun checkFileName(): String{
        return filename
    }



    // Update pen color
    fun updatePaintColor(color: Int){
        paint.color = color
    }

    // Update pen size
    fun updateSizeOfPaint(size: Float){
        size_of_paint = size
    }

    fun updateSizeOfEraser(size: Float){
        size_of_eraser = size
    }

    fun isInitialize(): Int {
        return initialize
    }

    //draws to our bitmap.
    fun draw(path: Path){
        when(paint_type){

            "circle" -> drawCircle(path)
            "rectangle" -> drawRectangle(path)
            "eraser" -> eraser(path)

        }
    }

    // Erase the Line that have draw
    private fun eraser(path: Path) {

        eraser_paint.strokeWidth = size_of_eraser
        eraser_paint.color = Color.WHITE
        eraser_paint.style = Paint.Style.STROKE
        eraser_paint.isAntiAlias = true
        eraser_paint.strokeJoin = Paint.Join.ROUND
        eraser_paint.strokeCap = Paint.Cap.ROUND
        bitmapCanvas.drawPath(path, eraser_paint)
        bitmap.value = bitmap.value

    }

    // Draw a circle on the bitmap at input location
    fun drawCircle(path: Path){

        paint.strokeWidth = size_of_paint
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        bitmapCanvas.drawPath(path, paint)
        bitmap.value = bitmap.value

    }

    // Draw a rectangle on the bitmap at input location
    fun  drawRectangle(path: Path){

        paint.strokeWidth = size_of_paint
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.SQUARE
//        paint.strokeCap = Paint.Cap.ROUND
        bitmapCanvas.drawPath(path, paint)
        bitmap.value = bitmap.value

    }

}

class DrawingBoardViewModelFactory(private val repository: DrawingBoardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrawingBoardModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DrawingBoardModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}