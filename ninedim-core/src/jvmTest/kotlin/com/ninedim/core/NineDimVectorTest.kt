package com.ninedim.core

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotEquals

class NineDimVectorTest {

    @Test
    fun `vector addition works correctly`() {
        val a = NineDimVector(d1 = 1.0, d2 = 2.0)
        val b = NineDimVector(d1 = 3.0, d7 = 4.0)
        val c = a + b
        assertEquals(4.0, c.d1, 0.001)
        assertEquals(2.0, c.d2, 0.001)
        assertEquals(4.0, c.d7, 0.001)
        assertEquals(0.0, c.d3, 0.001)
    }

    @Test
    fun `scalar multiplication works correctly`() {
        val a = NineDimVector(d1 = 1.0, d7 = 2.0)
        val b = a * 3.0
        assertEquals(3.0, b.d1, 0.001)
        assertEquals(6.0, b.d7, 0.001)
    }

    @Test
    fun `hadamard product works correctly`() {
        val a = NineDimVector(d1 = 2.0, d7 = 3.0)
        val b = NineDimVector(d1 = 3.0, d7 = 4.0)
        val c = a odot b
        assertEquals(6.0, c.d1, 0.001)
        assertEquals(12.0, c.d7, 0.001)
    }

    @Test
    fun `magnitude is calculated correctly`() {
        val v = NineDimVector(d1 = 3.0, d2 = 4.0)
        assertEquals(5.0, v.magnitude(), 0.001)
    }

    @Test
    fun `zero vector has zero magnitude`() {
        val v = NineDimVector()
        assertEquals(0.0, v.magnitude(), 0.001)
    }

    @Test
    fun `dominant dimension is identified correctly`() {
        val v = NineDimVector(d1 = 0.5, d7 = 5.0, d2 = 1.0)
        assertEquals(6, v.dominantDimension()) // D7 is index 6
    }

    @Test
    fun `dimension names are correct`() {
        assertEquals("容纳", NineDimVector.DIMENSION_NAMES[0])
        assertEquals("节律", NineDimVector.DIMENSION_NAMES[8])
        assertEquals(9, NineDimVector.DIMENSION_NAMES.size)
    }

    @Test
    fun `fromArray works correctly`() {
        val arr = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)
        val v = NineDimVector.fromArray(arr)
        assertEquals(1.0, v.d1, 0.001)
        assertEquals(9.0, v.d9, 0.001)
    }
}
