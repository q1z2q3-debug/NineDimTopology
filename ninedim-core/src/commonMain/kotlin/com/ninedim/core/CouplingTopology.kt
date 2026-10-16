package com.ninedim.core

/**
 * 十种配伍耦合拓扑类型
 */
enum class CouplingType(val displayName: String, val dimension: String, val representativeFormula: String, val description: String) {
    DEFLECTION("偏转型", "1D", "左金丸", "单一向量被引导偏转，引经报使的本质"),
    CIRCUIT("回路型", "2D", "交泰丸", "双向振荡平衡，如心肾相交"),
    TRIANGLE_VECTOR("三角矢量型", "3D", "四逆散", "三向量在三维空间合成新方向"),
    LAYERED_VERTICAL("分层协同型", "2D垂直", "黄连阿胶汤", "垂直层级组织，上清下补"),
    PARALLEL_HORIZONTAL("并置协同型", "2D水平", "柴胡桂枝汤", "水平模块并列，表里双解"),
    RING("环形耦合型", "闭环", "四君子汤", "首尾相接正向增益循环"),
    THREE_D_CONFRONTATION("三维对抗型", "3D对抗", "六味地黄丸", "补泻矢量动态张力平衡"),
    STAR("星型耦合型", "辐射", "炙甘草汤", "中心节点调节多方向张力"),
    TEMPORAL("时序耦合型", "时间轴", "大承气汤", "时间矢量接力，煎服时序"),
    BRIDGE("桥接型", "跨层", "小青龙汤(细辛)", "单节点同时属于多层，建立跨层通道");

    companion object {
        fun all() = values().toList()
    }
}

/**
 * 耦合对：两味药之间的耦合关系
 */
data class CouplingPair(
    val herbA: String,
    val herbB: String,
    val type: CouplingType,
    val kappa: Double = 1.0, // 相互作用系数
    val description: String = ""
)

/**
 * 拓扑分析器：识别方剂中的耦合拓扑类型
 */
object TopologyAnalyzer {

    /**
     * 分析一组药物向量，识别激活的耦合拓扑类型
     */
    fun analyze(herbs: List<Pair<String, NineDimVector>>): List<CouplingType> {
        if (herbs.size < 2) return emptyList()
        val result = mutableSetOf<CouplingType>()
        val vectors = herbs.map { it.second }

        // 检测回路型：存在两个方向相反的强向量
        if (hasOppositePair(vectors)) result.add(CouplingType.CIRCUIT)

        // 检测三维对抗型：存在两组方向相反的向量组
        if (hasConfrontationGroups(herbs)) result.add(CouplingType.THREE_D_CONFRONTATION)

        // 检测三角矢量型：>=3味药且向量方向各异
        if (vectors.size >= 3 && hasDiverseDirections(vectors)) {
            result.add(CouplingType.TRIANGLE_VECTOR)
        }

        // 检测环形耦合型：>=4味药且形成增益循环
        if (vectors.size >= 4) result.add(CouplingType.RING)

        // 检测星型耦合型：存在一个调和维度(D8)突出的药
        if (vectors.any { it.d8 > 1.0 }) result.add(CouplingType.STAR)

        // 检测偏转型：存在一个强向量和一个弱反向向量
        if (hasDeflection(vectors)) result.add(CouplingType.DEFLECTION)

        // 检测分层协同型：存在明显的上下层分工
        if (hasLayering(vectors)) result.add(CouplingType.LAYERED_VERTICAL)

        // 检测并置协同型：>=4味药且可分为两个独立模块
        if (vectors.size >= 4) result.add(CouplingType.PARALLEL_HORIZONTAL)

        // 检测桥接型：存在D7双向性的药
        if (vectors.any { kotlin.math.abs(it.d7) < 0.5 && it.d4 != 0.0 }) {
            result.add(CouplingType.BRIDGE)
        }

        return result.toList()
    }

    private fun hasOppositePair(vectors: List<NineDimVector>): Boolean {
        for (i in vectors.indices) {
            for (j in i+1 until vectors.size) {
                val dot = vectors[i].toArray().zip(vectors[j].toArray()).sumOf { it.first * it.second }
                if (dot < -2.0) return true
            }
        }
        return false
    }

    private fun hasConfrontationGroups(herbs: List<Pair<String, NineDimVector>>): Boolean {
        if (herbs.size < 4) return false
        val positiveCount = herbs.count { it.second.magnitude() > 1.0 && it.second.dominantDimension() in listOf(0, 2, 4) }
        val negativeCount = herbs.count { it.second.magnitude() > 1.0 && it.second.dominantDimension() in listOf(1, 3, 6) }
        return positiveCount >= 2 && negativeCount >= 2
    }

    private fun hasDiverseDirections(vectors: List<NineDimVector>): Boolean {
        val dominantDims = vectors.map { it.dominantDimension() }.toSet()
        return dominantDims.size >= 2
    }

    private fun hasDeflection(vectors: List<NineDimVector>): Boolean {
        val sorted = vectors.sortedByDescending { it.magnitude() }
        if (sorted.size < 2) return false
        val strong = sorted[0]
        val weak = sorted[1]
        return strong.magnitude() > 2.0 * weak.magnitude() && weak.magnitude() > 0.3
    }

    private fun hasLayering(vectors: List<NineDimVector>): Boolean {
        val upper = vectors.count { it.d2 > 0.5 || it.d4 > 0.5 }
        val lower = vectors.count { it.d1 > 0.5 || it.d5 > 0.5 }
        return upper >= 1 && lower >= 1
    }
}
