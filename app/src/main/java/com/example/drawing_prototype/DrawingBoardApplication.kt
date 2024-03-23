package com.example.drawing_prototype

import android.app.Application
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DrawingBoardApplication : Application() {
    val scope = CoroutineScope(SupervisorJob())

    val db by lazy { Room.databaseBuilder(applicationContext, DrawingBoardDatabase::class.java,
        "DrawingBoard_database"
    ).build()}

    val DrawingBoardRepository by lazy {DrawingBoardRepository(applicationContext, scope, db.drawingBoardDao())}

    //create our repository singleton, using lazy to access the DB when we need it
}