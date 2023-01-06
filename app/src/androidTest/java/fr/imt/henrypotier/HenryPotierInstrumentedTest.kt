package fr.imt.henrypotier

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.imt.henrypotier.data.BasketBook

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class HenryPotierInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("fr.imt.henrypotier", appContext.packageName)
    }

    @Test
    fun basket_total_is_correct() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        BasketService.removeAllBooks(appContext)
        val basketBook = BasketBook()
        basketBook.price = 10
        basketBook.isbn = "c8fabf68-8374-48fe-a7ea-a00ccd07afff"
        val basketBook2 = BasketBook()
        basketBook2.price = 20
        basketBook2.isbn = "a460afed-e5e7-4e39-a39d-c885c05db861"

        BasketService.addBooksToBasket(appContext, basketBook)
        BasketService.addBooksToBasket(appContext, basketBook2)
        assertEquals(30, BasketService.getTotalPrice(appContext).toInt())
    }
}