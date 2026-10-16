package com.ninedim.topology.core

/**
 * 逆向推导引擎：从病机反推最优耦合拓扑和候选方剂
 *
 * 病机 → 向量失衡描述 → 所需耦合拓扑 → 候选药组 → 候选方剂
 */
object ReverseEngineer {

    data class ReverseResult(
        val diseaseVector: NineDimVector,
        val requiredTopologies: List<CouplingType>,
        val candidateHerbs: List<String>,
        val candidateFormulas: List<String>,
        val explanation: String
    )

    /**
     * 从病机向量推导候选方剂
     */
    fun engineer(diseaseVector: NineDimVector, constitution: ConstitutionType = ConstitutionType.PING_HE): ReverseResult {
        // 叠加体质基底
        val actualVector = diseaseVector + constitution.vector * constitution.alpha

        // 分析向量失衡，确定所需拓扑
        val requiredTopologies = analyzeRequiredTopology(actualVector)

        // 匹配候选药组
        val candidateHerbs = matchHerbs(actualVector)

        // 匹配候选方剂
        val candidateFormulas = matchFormulas(actualVector, requiredTopologies)

        val explanation = buildExplanation(actualVector, requiredTopologies, candidateFormulas)

        return ReverseResult(
            diseaseVector = actualVector,
            requiredTopologies = requiredTopologies,
            candidateHerbs = candidateHerbs,
            candidateFormulas = candidateFormulas,
            explanation = explanation
        )
    }

    private fun analyzeRequiredTopology(vec: NineDimVector): List<CouplingType> {
        val result = mutableListOf<CouplingType>()
        val dominant = vec.dominantDimension()
        val magnitude = vec.magnitude()

        // 单向偏盛 → 偏转型
        if (magnitude > 3.0 && countStrongDimensions(vec) == 1) {
            result.add(CouplingType.DEFLECTION)
        }

        // 双向失调 → 回路型
        if (kotlin.math.abs(vec.d2) > 1.0 && kotlin.math.abs(vec.d7) > 1.0) {
            result.add(CouplingType.CIRCUIT)
        }

        // 多向紊乱 → 三角矢量型
        if (countStrongDimensions(vec) >= 3) {
            result.add(CouplingType.TRIANGLE_VECTOR)
        }

        // 表里同病 → 分层+桥接
        if (kotlin.math.abs(vec.d4) > 1.0 && kotlin.math.abs(vec.d1) > 1.0) {
            result.add(CouplingType.LAYERED_VERTICAL)
            result.add(CouplingType.BRIDGE)
        }

        // 虚损为主 → 环形耦合
        if (vec.d1 < -1.0 && magnitude < 4.0) {
            result.add(CouplingType.RING)
        }

        // 虚实夹杂 → 三维对抗
        if (vec.d1 < -0.5 && vec.d2 > 0.5) {
            result.add(CouplingType.THREE_D_CONFRONTATION)
        }

        // 多系统失调 → 星型
        if (countStrongDimensions(vec) >= 4) {
            result.add(CouplingType.STAR)
        }

        // 急证实邪 → 时序耦合
        if (magnitude > 5.0) {
            result.add(CouplingType.TEMPORAL)
        }

        if (result.isEmpty()) result.add(CouplingType.CIRCUIT)
        return result.distinct()
    }

    private fun countStrongDimensions(vec: NineDimVector): Int {
        return vec.toArray().count { kotlin.math.abs(it) > 1.0 }
    }

    private fun matchHerbs(vec: NineDimVector): List<String> {
        val herbs = mutableListOf<String>()
        if (vec.d7 > 1.0) herbs.addAll(listOf("麻黄", "桂枝", "柴胡"))
        if (vec.d7 < -1.0) herbs.addAll(listOf("大黄", "枳实", "半夏"))
        if (vec.d2 > 1.0) herbs.addAll(listOf("黄连", "黄芩", "石膏"))
        if (vec.d2 < -1.0) herbs.addAll(listOf("附子", "干姜", "肉桂"))
        if (vec.d1 < -1.0) herbs.addAll(listOf("人参", "黄芪", "白术"))
        if (vec.d5 > 1.0) herbs.addAll(listOf("五味子", "芍药", "山茱萸"))
        if (vec.d8 > 0.5) herbs.add("甘草")
        return herbs.distinct().take(8)
    }

    private fun matchFormulas(vec: NineDimVector, topologies: List<CouplingType>): List<String> {
        val formulas = mutableListOf<String>()

        when {
            vec.d7 > 2.0 && vec.d2 < 0 -> formulas.add("麻黄汤")
            vec.d7 > 1.0 && kotlin.math.abs(vec.d5) > 0.5 -> formulas.add("桂枝汤")
            vec.d2 > 2.0 && vec.d4 > 1.0 -> formulas.add("白虎汤")
            vec.d6 > 1.0 && vec.d2 > 0 -> formulas.add("小柴胡汤")
            vec.d1 < -1.0 && vec.d7 < -1.0 -> formulas.add("四君子汤")
            vec.d1 < -1.0 && vec.d2 > 0.5 -> formulas.add("六味地黄丸")
            vec.d2 > 1.0 && vec.d7 < -1.0 -> formulas.add("大承气汤")
            vec.d4 > 1.0 && vec.d1 < -0.5 -> formulas.add("小青龙汤")
            vec.d2 > 0 && vec.d2 < 2.0 && vec.d7 > 0 -> formulas.add("逍遥散")
            vec.d2 < -1.0 && vec.d1 < -0.5 -> formulas.add("真武汤")
        }

        if (formulas.isEmpty()) formulas.add("桂枝汤")
        return formulas.take(4)
    }

    private fun buildExplanation(vec: NineDimVector, topologies: List<CouplingType>, formulas: List<String>): String {
        val dominantName = NineDimVector.DIMENSION_NAMES[vec.dominantDimension()]
        return buildString {
            append("病机向量以")
            append(dominantName)
            append("维度（D${vec.dominantDimension() + 1}）最为突出，模长为 %.2f。".format(vec.magnitude()))
            append("需要激活 ${topologies.size} 种耦合拓扑：")
            append(topologies.joinToString("、") { it.displayName })
            append("。推荐候选方剂：")
            append(formulas.joinToString("、"))
            append("。")
        }
    }
}

/**
 * 体质类型
 */
enum class ConstitutionType(
    val displayName: String,
    val vector: NineDimVector,
    val alpha: Double = 0.8,
    val description: String
) {
    PING_HE("平和质", NineDimVector(), 0.0, "基准零向量，阴阳平衡"),
    QI_XU("气虚质", NineDimVector(d1 = -2.0, d2 = -1.0, d7 = -2.0), 0.8, "D1/D7双低，推动力不足"),
    YANG_XU("阳虚质", NineDimVector(d1 = -1.0, d2 = -3.0, d7 = -1.0), 0.8, "D2极低，恶寒重"),
    YIN_XU("阴虚质", NineDimVector(d1 = -2.0, d2 = 1.0, d5 = -1.0), 0.6, "D1不足、D2虚热"),
    TAN_SHI("痰湿质", NineDimVector(d1 = 1.0, d2 = -1.0, d5 = 3.0, d7 = -1.0), 0.7, "D5过度固化"),
    SHI_RE("湿热质", NineDimVector(d2 = 2.0, d5 = 1.0, d7 = -1.0), 0.7, "D2热+D5湿"),
    XUE_YU("血瘀质", NineDimVector(d5 = 3.0, d7 = -2.0), 0.8, "D5固化、D7受阻"),
    QI_YU("气郁质", NineDimVector(d7 = -3.0), 0.8, "D7严重受阻"),
    TE_BING("特禀质", NineDimVector(d1 = -1.0, d7 = 1.0), 0.9, "D1不足、D7过敏");

    companion object {
        fun all() = values().toList()
    }
}
