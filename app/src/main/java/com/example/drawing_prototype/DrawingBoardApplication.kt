package com.example.drawing_prototype

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DrawingBoardApplication : Application() {
    val scope = CoroutineScope(SupervisorJob())

    //get a reference to the DB singleton
    //val db by lazy {FunFactDatabase.getDatabase(applicationContext)}
//    val db by lazy {
//        Room.databaseBuilder(
//        applicationContext,
//        DrawingBoardDatabase::class.java,
//        "drawing_board_database"
//    ).build()}

    //create our repository singleton, using lazy to access the DB when we need it
    val boardRepository by lazy {DrawingBoardRepository(applicationContext, scope)}
}