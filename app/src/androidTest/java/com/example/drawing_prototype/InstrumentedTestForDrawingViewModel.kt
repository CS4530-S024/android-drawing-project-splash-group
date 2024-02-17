package com.example.drawing_prototype

import androidx.lifecycle.Observer
import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTestForDrawingViewModel {
    private lateinit var model: DrawingBoardModel
    private lateinit var lifecycleOwner: TestLifecycleOwner

    /**
     * initialize the model
     */
    @Before
    fun initialize(){
        model = DrawingBoardModel().apply {
            initializeModel()
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
            model.drawCircle(100f,100f)
            assertTrue("Draw a circle can be triggered bitmap observer",callbackFired)
            model.bitmap.removeObserver(observer)

            //test draw rectangle
            callbackFired = false
            model.bitmap.observe(lifecycleOwner,observer)
            model.drawRectangle(100f,100f)
            assertTrue("Draw a rectangle can be triggered bitmap observer",callbackFired)
            model.bitmap.removeObserver(observer)

        }
    }
}