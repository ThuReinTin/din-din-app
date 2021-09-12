package tin.thurein.data

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        val i = 3
        if(i > 1) {
            println("i is greater than 1")
        }
        if (i > 2) { println("i is greater than 2")
           println("i is greater than 3")
        }
        println("Hello WOrld")
    }
}