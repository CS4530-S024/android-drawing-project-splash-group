package com.example.drawing_prototype

//import org.mockito.Mockito.mock


import android.R.attr.path
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Path
import androidx.lifecycle.Observer
import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.withContext
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTestForDrawingViewModel {
    private lateinit var model: DrawingBoardModel
    private lateinit var lifecycleOwner: TestLifecycleOwner
    private lateinit var repository: DrawingBoardRepository
    private lateinit var context: Context
    private val testScope = TestCoroutineScope()

    /**
     * initialize the model
     */
    @Before
    fun initialize(){

        context = ApplicationProvider.getApplicationContext<Context>()

        // Build an in-memory Room database
        val db = Room.inMemoryDatabaseBuilder(
            context, DrawingBoardDatabase::class.java).allowMainThreadQueries().build()
        val dao = db.drawingBoardDao()


        repository = DrawingBoardRepository(context, testScope, dao)
        model = DrawingBoardModel(repository).apply {
            initializeModel(1100, 1100)
        }

        lifecycleOwner = TestLifecycleOwner()
    }

    /**
     * test the view model functional(paint type, paint color, paint size, draw circle and rectangle)
     */
    @Test
    fun testModelFunctional() = runBlocking {
        withContext(Dispatchers.Main){
            var callbackFired = false
            val observer = Observer<android.graphics.Bitmap> {callbackFired = true}
            val path = Path()
            path.moveTo(100f, 100f)
            path.lineTo(200f, 100f);

            //test initialize
            assertNotNull("Bitmap can not be null after initialization", model.bitmap.value)

            //test paint update type
            val initialType = model.paint_type
            val newType = "rectangle"
            model.updateType(newType)
            assertNotSame("Paint type should change after update type",initialType,model.paint_type)

            //test paint update color
            val initialColor = model.paint.color
            val newColor = android.graphics.Color.RED
            model.updatePaintColor(newColor)
            assertNotSame("Paint color should change after update color",initialColor,model.paint.color)

            //test paint update size
            val initialSize = model.size_of_paint
            val newSize = 50.0f
            model.updateSizeOfPaint(newSize)
            assertNotSame("Paint size should change after update size",initialSize,model.size_of_paint)

            //test draw circle
            callbackFired = false
            model.bitmap.observe(lifecycleOwner,observer)
            model.drawCircle(path)
            assertTrue("Draw a circle can be triggered bitmap observer",callbackFired)
            model.bitmap.removeObserver(observer)

            //test draw rectangle
            callbackFired = false
            model.bitmap.observe(lifecycleOwner,observer)
            model.drawRectangle(path)
            assertTrue("Draw a rectangle can be triggered bitmap observer",callbackFired)
            model.bitmap.removeObserver(observer)


            //Test whether it can store image correct
            val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888) // Create a test bitmap
            val fileName = "test_image"
            val savedDrawingBoard = repository.savePicture(bitmap, fileName)
            // Verify the file was created
            val file = File(context.filesDir, "$fileName.png")
            assert(file.exists())
            // Verify the DrawingBoard entity was saved
            assertNotNull(savedDrawingBoard)


            // Test whether it can load Bitmap correct
            val test_load_bitmap = repository.loadBitmap("test_image")
            fun bitmapsAreEqual(first: Bitmap, second: Bitmap): Boolean {
                if (first.width != second.width || first.height != second.height) return false

                for (y in 0 until first.height) {
                    for (x in 0 until first.width) {
                        if (first.getPixel(x, y) != second.getPixel(x, y)) return false
                    }
                }
                return true
            }
            assertTrue("Loaded bitmap does not match the original", bitmapsAreEqual(bitmap, test_load_bitmap))

        }
    }


}