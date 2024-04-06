package com.example.drawing_prototype

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream

class DrawingBoardRepository(private val context: Context, private val scope: CoroutineScope, private val dao: DrawingBoardDao){

    private var drawingBoardLiveData: LiveData<List<DrawingBoard>>? = null;

    val allDrawingBoard = dao.allDawingBoard()

    // This will load a picture from repository
    fun loadBitmap(fileName: String): Bitmap {
        val filePath = File(context.filesDir, "$fileName.png")
        return  BitmapFactory.decodeFile(filePath.absolutePath)
    }

    // This will save the picture in repository
    suspend fun savePicture(bitmap: Bitmap, fileName: String): DrawingBoard? {
        return try {
            val file = File(context.filesDir, "$fileName.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            val DrawingBoard = DrawingBoard(fileName, System.currentTimeMillis())
            dao.apply {
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

    // This will save the picture in repository
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

    suspend fun deletePicture(fileName: String): Boolean {
        return try {
            val picture = dao.getPictureByFileName(fileName)
            if (picture != null) {
                dao.deletePicture(picture)

                val file = File(context.filesDir, "$fileName.png")
                if (file.exists()) {
                    file.delete()
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("DrawingBoardRepository", "Error deleting picture: ${e.message}")
            false
        }
    }

    fun removePicture(fileName: String) {
        scope.launch {
            val isDeleted = deletePicture(fileName)
            if (isDeleted) {
                Log.d("DrawingBoardRepository", "Picture deleted successfully")
            } else {
                Log.e("DrawingBoardRepository", "Error deleting picture")
            }
        }
    }
}