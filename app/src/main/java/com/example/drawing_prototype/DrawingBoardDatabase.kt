 package com.example.drawing_prototype

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

//this is a DB, we have 1 entity (so we'll get 1 table in SQLite)
//the version stuff is for managing DB migrations

@Database(entities= [DrawingBoard::class], version = 1, exportSchema = false)
abstract class DrawingBoardDatabase : RoomDatabase(){
    abstract fun drawingBoardDao(): DrawingBoardDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DrawingBoardDatabase? = null

        //When we want a DB we call this (basically static) method
        fun getDatabase(context: Context): DrawingBoardDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                //if another thread initialized this before we got the lock
                //return the object they created
                if(INSTANCE != null) return INSTANCE!!
                //otherwise we're the first thread here, so create the DB
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrawingBoardDatabase::class.java,
                    "drawing_board_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}


@Dao
interface DrawingBoardDao {
    @Insert
    suspend fun insertPicture(picture: DrawingBoard)

    @Delete
    suspend fun deletePicture(picture: DrawingBoard)

    @Query("SELECT * FROM drawing_board where fileName=:fileName")
    fun getPictureByFileName(fileName: String): DrawingBoard?

    @Query("UPDATE drawing_board SET timestamp=:timestamp WHERE id=:id")
    fun updatePicture(timestamp: Long, id: Int)

    @Query("SELECT * FROM drawing_board ORDER BY fileName")
    //fun getAllPicture(): LiveData<List<DrawingBoard>>
    fun allDawingBoard(): Flow<List<DrawingBoard>>


}