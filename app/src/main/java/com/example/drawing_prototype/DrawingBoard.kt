package com.example.drawing_prototype

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class DrawingBoard(val id: Int, val filePath: String, val timestamp: Long)

@Entity(tableName = "drawing_board")
data class DrawingBoardEntity(var filePath: String, var timestamp: Long) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0 // Integer primary key for the database
}