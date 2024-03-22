package com.example.drawing_prototype

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "drawing_board")
data class DrawingBoard(var fileName: String, var timestamp: Long) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0 // Integer primary key for the database
}