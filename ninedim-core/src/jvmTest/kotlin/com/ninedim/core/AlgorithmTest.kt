package com.ninedim.core

import com.ninedim.core.data.HerbDatabase
import com.ninedim.core.data.FormulaDatabase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertNotEquals

class FormulaEngineTest {

    @Test
    fun `mahuang tang demo amplifies D7 by 2_7x`() {
        val result = FormulaEngine.mahuangTangDemo()
        val without = result["withoutInteraction_D7"] as Double
        val with = result["withInteraction_D7"] as Double
        val amplification = result["amplification"] as Double

        assertTrue(with > without, "With interaction should be larger")
        assertTrue(amplification > 2.0, "Amplification should be > 2.0, got $amplification")
    }

    @Test
    fun `simple synthesis adds weighted vectors`() {
        val v1 = NineDimVector(d7 = 1.0)
        val v2 = NineDimVector(d7 = 2.0)
        val result = FormulaEngine.simpleSynthesize(listOf(v1 to 2.0, v2 to 1.0))
        assertEquals(4.0, result.d7, 0.001) // 1*2 + 2*1 = 4
    }
}

class HerbDatabaseTest {

    @Test
    fun `database contains herbs`() {
        assertTrue(HerbDatabase.allHerbs.size >= 30)
    }

    @Test
    fun `find herb by name works`() {
        val mahuang = HerbDatabase.findByName("麻黄")
        assertNotNull(mahuang)
        assertEquals("Mahuang", mahuang!!.pinyin)
    }

    @Test
    fun `search herbs by query`() {
        val results = HerbDatabase.search("黄")
        assertTrue(results.size >= 2) // 黄连, 黄芩, 黄芪, 大黄
    }

    @Test
    fun `herb has valid vector`() {
        val gancao = HerbDatabase.findByName("甘草")
        assertNotNull(gancao)
        assertTrue(gancao!!.vector.d8 > 0, "甘草 should have D8调和 component")
    }
}

class FormulaDatabaseTest {

    @Test
    fun `database contains enough formulas`() {
        assertTrue(FormulaDatabase.allFormulas.size >= 25)
    }

    @Test
    fun `find formula by name works`() {
        val guizhi = FormulaDatabase.findByName("桂枝汤")
        assertNotNull(guizhi)
        assertTrue(guizhi!!.couplingTypes.isNotEmpty())
    }

    @Test
    fun `formula synthesis produces non-zero vector`() {
        val guizhi = FormulaDatabase.findByName("桂枝汤")
        assertNotNull(guizhi)
        val vec = guizhi!!.synthesizedVector()
        assertTrue(vec.magnitude() > 0, "Synthesized vector should be non-zero")
    }
}

class ReverseEngineerTest {

    @Test
    fun `reverse engineering returns result`() {
        val disease = NineDimVector(d7 = 2.0, d2 = -1.0)
        val result = ReverseEngineer.engineer(disease)
        assertTrue(result.requiredTopologies.isNotEmpty())
        assertTrue(result.candidateFormulas.isNotEmpty())
        assertTrue(result.candidateHerbs.isNotEmpty())
    }

    @Test
    fun `constitution affects result`() {
        val disease = NineDimVector(d7 = 2.0)
        val pinghe = ReverseEngineer.engineer(disease, ConstitutionType.PING_HE)
        val qixu = ReverseEngineer.engineer(disease, ConstitutionType.QI_XU)
        // Different constitutions should produce different adjusted vectors
        assertNotEquals(pinghe.diseaseVector.d1, qixu.diseaseVector.d1, 0.001)
    }

    @Test
    fun `nine constitution types exist`() {
        assertEquals(9, ConstitutionType.all().size)
    }
}
