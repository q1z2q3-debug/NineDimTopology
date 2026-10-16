package com.ninedim.topology.core

/**
 * 九维向量：描述药物/方剂/病机的药效方向
 * D1 容纳 | D2 温度 | D3 润燥 | D4 表达 | D5 固化
 * D6 结构 | D7 运动 | D8 调和 | D9 节律
 */
data class NineDimVector(
    val d1: Double = 0.0, // 容纳：补益滋养(+) / 泻下耗散(-)
    val d2: Double = 0.0, // 温度：温热(+) / 寒凉(-)
    val d3: Double = 0.0, // 润燥：生津(+) / 燥湿(-)
    val d4: Double = 0.0, // 表达：发散宣通(+) / 内敛收敛(-)
    val d5: Double = 0.0, // 固化：收敛固涩(+) / 消散破结(-)
    val d6: Double = 0.0, // 结构：归经定位
    val d7: Double = 0.0, // 运动：升浮向外(+) / 沉降向内(-)
    val d8: Double = 0.0, // 调和：信息整合
    val d9: Double = 0.0  // 节律：周期调节
) {
    operator fun plus(other: NineDimVector) = NineDimVector(
        d1 + other.d1, d2 + other.d2, d3 + other.d3,
        d4 + other.d4, d5 + other.d5, d6 + other.d6,
        d7 + other.d7, d8 + other.d8, d9 + other.d9
    )

    operator fun times(scalar: Double) = NineDimVector(
        d1 * scalar, d2 * scalar, d3 * scalar,
        d4 * scalar, d5 * scalar, d6 * scalar,
        d7 * scalar, d8 * scalar, d9 * scalar
    )

    /** 哈达玛积（逐元素相乘） */
    infix fun odot(other: NineDimVector) = NineDimVector(
        d1 * other.d1, d2 * other.d2, d3 * other.d3,
        d4 * other.d4, d5 * other.d5, d6 * other.d6,
        d7 * other.d7, d8 * other.d8, d9 * other.d9
    )

    /** 模长 */
    fun magnitude(): Double = kotlin.math.sqrt(
        d1* d1 + d2* d2 + d3* d3 + d4* d4 + d5* d5 +
        d6* d6 + d7* d7 + d8* d8 + d9* d9
    )

    /** 获取最突出的维度索引 (0-8) */
    fun dominantDimension(): Int {
        val vals = doubleArrayOf(d1, d2, d3, d4, d5, d6, d7, d8, d9)
        var maxIdx = 0
        var maxAbs = kotlin.math.abs(vals[0])
        for (i in 1 until 9) {
            if (kotlin.math.abs(vals[i]) > maxAbs) {
                maxAbs = kotlin.math.abs(vals[i])
                maxIdx = i
            }
        }
        return maxIdx
    }

    fun toArray() = doubleArrayOf(d1, d2, d3, d4, d5, d6, d7, d8, d9)

    companion object {
        val DIMENSION_NAMES = listOf("容纳", "温度", "润燥", "表达", "固化", "结构", "运动", "调和", "节律")
        val DIMENSION_SYMBOLS = listOf("D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9")

        fun fromArray(arr: DoubleArray) = NineDimVector(
            arr.getOrElse(0){0.0}, arr.getOrElse(1){0.0}, arr.getOrElse(2){0.0},
            arr.getOrElse(3){0.0}, arr.getOrElse(4){0.0}, arr.getOrElse(5){0.0},
            arr.getOrElse(6){0.0}, arr.getOrElse(7){0.0}, arr.getOrElse(8){0.0}
        )
    }
}
