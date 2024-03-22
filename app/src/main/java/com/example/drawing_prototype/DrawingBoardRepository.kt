package com.example.drawing_prototype

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class DrawingBoardRepository(
    private val context: Context,
    private val scope: CoroutineScope){

    private var drawingBoardLiveData: LiveData<List<DrawingBoard>>? = null;
    private var drawingBoardDao: DrawingBoardDao? = null

    init {
        val db: DrawingBoardDatabase = DrawingBoardDatabase.getDatabase(context.applicationContext)
        drawingBoardDao = db.drawingBoardDao()
    }

    fun getAllPicture(): LiveData<List<DrawingBoard>>? {
        return drawingBoardDao?.getAllPicture()
    }

    suspend fun savePicture(bitmap: Bitmap, fileName: String): DrawingBoard? {
        return try {
            val file = File(context.filesDir, "$fileName.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            val DrawingBoard = DrawingBoard(fileName, System.currentTimeMillis())
            drawingBoardDao?.apply {
                val query = getPictureByFileName(fileName)

                if (query == null) {
                    insertPicture(DrawingBoard)
                } else {
                    updatePicture(System.currentTimeMillis(), query.id)
                }


            }
            DrawingBoard
        } catch (e: Exception) {
            Log.e("DrawingBoardRepository", "Error saving picture: ${e.message}")
            null
        }
    }


    fun storePicture(bitmap: Bitmap, fileName: String) {
        scope.launch {
            val savedEntity = savePicture(bitmap, fileName)
            if (savedEntity != null) {
                Log.d("DrawingBoardRepository", "Picture saved successfully with ID ${savedEntity.id}")
            } else {
                Log.e("DrawingBoardRepository", "Error saving picture")
            }
        }
    }


}