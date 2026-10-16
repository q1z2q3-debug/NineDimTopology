package com.ninedim.core

/**
 * 七情关系的相互作用系数 κ
 */
enum class SevenEmotions(val displayName: String, val kappaRange: ClosedRange<Double>, val description: String) {
    MUTUAL_PROMOTION("相须", 1.3..1.8, "同类协同，效应放大"),
    MUTUAL_ASSISTANCE("相使", 1.1..1.4, "主辅配合，辅助增效"),
    MUTUAL_INHIBITION("相畏", 0.3..0.6, "毒性被抑制"),
    MUTUAL_DETOXIFICATION("相杀", 0.3..0.6, "毒性被中和"),
    OPPOSITION("相反", -1.0..-0.5, "效应抵消甚至产生新毒性"),
    INCOMPATIBILITY("相恶", 0.5..0.8, "效应被削弱"),
    SINGLE("单行", 1.0..1.0, "无相互作用");

    companion object {
        fun defaultKappa(emotion: SevenEmotions): Double = when (emotion) {
            MUTUAL_PROMOTION -> 1.6
            MUTUAL_ASSISTANCE -> 1.2
            MUTUAL_INHIBITION -> 0.5
            MUTUAL_DETOXIFICATION -> 0.5
            OPPOSITION -> -0.8
            INCOMPATIBILITY -> 0.65
            SINGLE -> 1.0
        }
    }
}

/**
 * 向量合成引擎：包含剂量权重和药物相互作用系数
 *
 * V_j = Σ(w_i × v_ij) + Σ(κ_ik × w_i × w_k × (v_ij ⊗ v_kj))
 */
object FormulaEngine {

    /**
     * 计算方剂的合成向量
     * @param herbs 药物列表：(名称, 九维向量, 剂量权重)
     * @param interactions 药对相互作用：(药A, 药B, κ系数)
     */
    fun synthesize(
        herbs: List<Triple<String, NineDimVector, Double>>,
        interactions: List<Triple<String, String, Double>> = emptyList()
    ): NineDimVector {
        // 第一项：单药剂量加权和
        var result = NineDimVector()
        for ((_, vec, weight) in herbs) {
            result = result + (vec * weight)
        }

        // 第二项：药对耦合修正
        for ((a, b, kappa) in interactions) {
            val herbA = herbs.find { it.first == a }
            val herbB = herbs.find { it.first == b }
            if (herbA != null && herbB != null) {
                val coupling = herbA.second odot herbB.second
                val correction = coupling * (kappa * herbA.third * herbB.third)
                result = result + correction
            }
        }

        return result
    }

    /**
     * 简单合成（不考虑相互作用）
     */
    fun simpleSynthesize(herbs: List<Pair<NineDimVector, Double>>): NineDimVector {
        var result = NineDimVector()
        for ((vec, weight) in herbs) {
            result = result + (vec * weight)
        }
        return result
    }

    /**
     * 麻黄汤验证示例：相须耦合使发汗效应放大2.7倍
     */
    fun mahuangTangDemo(): Map<String, Any> {
        val mahuang = NineDimVector(d2 = 1.0, d4 = 3.0, d7 = 3.0)
        val guizhi = NineDimVector(d2 = 1.0, d4 = 2.0, d7 = 2.0)
        val xingren = NineDimVector(d7 = -1.0)
        val gancao = NineDimVector(d1 = 1.0, d8 = 2.0)

        val herbs = listOf(
            Triple("麻黄", mahuang, 3.0),
            Triple("桂枝", guizhi, 2.0),
            Triple("杏仁", xingren, 0.7),
            Triple("甘草", gancao, 1.0)
        )

        // 无相互作用
        val withoutInteraction = simpleSynthesize(herbs.map { Pair(it.second, it.third) })

        // 有相互作用：麻黄+桂枝相须 κ=1.6，麻黄+杏仁相使 κ=1.2，麻黄+甘草相畏 κ=0.5
        val interactions = listOf(
            Triple("麻黄", "桂枝", 1.6),
            Triple("麻黄", "杏仁", 1.2),
            Triple("麻黄", "甘草", 0.5)
        )
        val withInteraction = synthesize(herbs, interactions)

        return mapOf(
            "withoutInteraction_D7" to withoutInteraction.d7,
            "withInteraction_D7" to withInteraction.d7,
            "amplification" to withInteraction.d7 / withoutInteraction.d7
        )
    }
}
