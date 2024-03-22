package com.example.drawing_prototype

import android.graphics.Bitmap
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class DrawingBoardRepository(private val scope: CoroutineScope,
                             private val dao: DrawingBoardDao,
                             private val filesDir: File){
    val picturesList: Flow<List<DrawingBoardEntity>> = dao.getAllPicture()

    suspend fun savePicture(bitmap: Bitmap): DrawingBoardEntity? {
        return try {
            val fileName = "${System.currentTimeMillis()}.png"
            val file = File(filesDir, fileName)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            val drawingBoardEntity = DrawingBoardEntity(fileName, System.currentTimeMillis())
            dao.insertPicture(drawingBoardEntity)
            drawingBoardEntity
        } catch (e: Exception) {
            Log.e("DrawingBoardRepository", "Error saving picture: ${e.message}")
            null
        }
    }


    fun storePicture(bitmap: Bitmap) {
        scope.launch {
            val savedEntity = savePicture(bitmap)
            if (savedEntity != null) {
                Log.d("DrawingBoardRepository", "Picture saved successfully with ID ${savedEntity.id}")
            } else {
                Log.e("DrawingBoardRepository", "Error saving picture")
            }
        }
    }


}