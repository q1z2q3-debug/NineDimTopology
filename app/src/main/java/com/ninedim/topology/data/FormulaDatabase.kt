package com.ninedim.topology.data

import com.ninedim.topology.core.CouplingType
import com.ninedim.topology.core.NineDimVector

/**
 * 方剂数据类
 */
data class Formula(
    val name: String,
    val pinyin: String,
    val source: String,
    val composition: List<Pair<String, Double>>, // 药名 + 剂量(两)
    val effects: String,
    val indications: String,
    val couplingTypes: List<CouplingType>,
    val description: String
) {
    /** 获取方剂的合成向量（简单加权） */
    fun synthesizedVector(): NineDimVector {
        var result = NineDimVector()
        for ((herbName, dose) in composition) {
            val herb = HerbDatabase.findByName(herbName)
            if (herb != null) {
                result = result + (herb.vector * dose)
            }
        }
        return result
    }
}

/**
 * 内置经典方剂数据库
 */
object FormulaDatabase {
    val allFormulas = listOf(
        Formula("桂枝汤", "Guizhi Tang", "《伤寒论》",
            listOf("桂枝" to 3.0, "芍药" to 3.0, "生姜" to 3.0, "大枣" to 2.0, "甘草" to 2.0),
            "解肌发表，调和营卫", "外感风寒表虚证，头痛发热，汗出恶风",
            listOf(CouplingType.CIRCUIT, CouplingType.STAR, CouplingType.LAYERED_VERTICAL, CouplingType.TEMPORAL),
            "桂枝芍药等量，散收振荡平衡；甘草星型阻尼；姜枣分层托举中焦"),

        Formula("麻黄汤", "Mahuang Tang", "《伤寒论》",
            listOf("麻黄" to 3.0, "桂枝" to 2.0, "杏仁" to 0.7, "甘草" to 1.0),
            "发汗解表，宣肺平喘", "外感风寒表实证，恶寒发热，无汗而喘",
            listOf(CouplingType.DEFLECTION, CouplingType.STAR),
            "麻黄桂枝相须，发汗效应放大2.7倍；甘草相畏缓和峻烈"),

        Formula("小青龙汤", "Xiaoqinglong Tang", "《伤寒论》",
            listOf("麻黄" to 3.0, "芍药" to 3.0, "细辛" to 3.0, "干姜" to 3.0, "甘草" to 3.0, "桂枝" to 3.0, "五味子" to 2.0, "半夏" to 2.0),
            "解表散寒，温肺化饮", "外寒内饮证，恶寒发热，无汗，喘咳，痰多清稀",
            listOf(CouplingType.THREE_D_CONFRONTATION, CouplingType.STAR, CouplingType.BRIDGE, CouplingType.LAYERED_VERTICAL),
            "六型耦合同时激活：外层散vs内层收的三维对抗，甘草星型阻尼，细辛桥接表里"),

        Formula("四君子汤", "Sijunzi Tang", "《太平惠民和剂局方》",
            listOf("人参" to 3.0, "白术" to 3.0, "茯苓" to 3.0, "甘草" to 2.0),
            "益气健脾", "脾胃气虚证，面色萎白，语声低微，气短乏力",
            listOf(CouplingType.RING, CouplingType.STAR),
            "四药首尾相接正向增益循环：人参补气→白术运化→茯苓渗湿→反哺补气"),

        Formula("六味地黄丸", "Liuwei Dihuang Wan", "《小儿药证直诀》",
            listOf("熟地黄" to 4.0, "山茱萸" to 2.0, "山药" to 2.0, "泽泻" to 1.5, "牡丹皮" to 1.5, "茯苓" to 1.5),
            "滋阴补肾", "肾阴亏损证，头晕耳鸣，腰膝酸软，骨蒸潮热",
            listOf(CouplingType.THREE_D_CONFRONTATION, CouplingType.LAYERED_VERTICAL),
            "三补三泻三维对抗：熟地山茱萸山药向内充盈，泽泻丹皮茯苓向外泄浊"),

        Formula("白虎汤", "Baihu Tang", "《伤寒论》",
            listOf("石膏" to 4.0, "甘草" to 1.0),
            "清热生津", "阳明气分热盛证，壮热面赤，烦渴引饮，汗出恶热",
            listOf(CouplingType.STAR, CouplingType.DEFLECTION),
            "石膏为星型中心大清气分热，D2强负但D7近乎零，清热但不冰伏"),

        Formula("小柴胡汤", "Xiaochaihu Tang", "《伤寒论》",
            listOf("柴胡" to 3.0, "黄芩" to 2.0, "人参" to 2.0, "甘草" to 2.0, "半夏" to 2.0, "生姜" to 2.0, "大枣" to 2.0),
            "和解少阳", "伤寒少阳证，往来寒热，胸胁苦满，默默不欲饮食",
            listOf(CouplingType.CIRCUIT, CouplingType.STAR, CouplingType.LAYERED_VERTICAL),
            "柴胡升+黄芩降，重建少阳枢机振荡回路；人参甘草扶正托底"),

        Formula("大承气汤", "Dachengqi Tang", "《伤寒论》",
            listOf("大黄" to 2.0, "厚朴" to 3.0, "枳实" to 3.0, "芒硝" to 2.0),
            "峻下热结", "阳明腑实证，大便不通，频转矢气，脘腹痞满",
            listOf(CouplingType.TEMPORAL, CouplingType.DEFLECTION),
            "时序耦合：先煮枳朴破气开道→后下大黄泻下→芒硝溶服软坚润燥"),

        Formula("交泰丸", "Jiaotai Wan", "《韩氏医通》",
            listOf("黄连" to 3.0, "桂枝" to 1.0),
            "交通心肾", "心火偏亢，心肾不交证，怔忡不宁，夜寐不安",
            listOf(CouplingType.CIRCUIT),
            "黄连清心火+桂枝温肾阳，重建心肾相交的双向振荡回路"),

        Formula("左金丸", "Zuojin Wan", "《丹溪心法》",
            listOf("黄连" to 6.0, "桂枝" to 1.0),
            "清泻肝火，降逆止呕", "肝火犯胃证，胁肋疼痛，嘈杂吞酸，呕吐口苦",
            listOf(CouplingType.DEFLECTION),
            "黄连:吴茱萸=6:1，吴茱萸引导黄连清热方向从清心胃偏转为清肝火"),

        Formula("逍遥散", "Xiaoyao San", "《太平惠民和剂局方》",
            listOf("柴胡" to 2.0, "当归" to 2.0, "芍药" to 2.0, "白术" to 2.0, "茯苓" to 2.0, "甘草" to 1.0, "生姜" to 1.0),
            "疏肝解郁，养血健脾", "肝郁血虚脾弱证，两胁作痛，头痛目眩，口燥咽干",
            listOf(CouplingType.TRIANGLE_VECTOR, CouplingType.LAYERED_VERTICAL, CouplingType.STAR),
            "柴胡疏肝+当归白芍养血+白术茯苓健脾，三角矢量合成新方向"),

        Formula("真武汤", "Zhenwu Tang", "《伤寒论》",
            listOf("附子" to 2.0, "茯苓" to 3.0, "白术" to 2.0, "芍药" to 2.0, "生姜" to 3.0),
            "温阳利水", "阳虚水泛证，畏寒肢厥，小便不利，心下悸动不宁",
            listOf(CouplingType.LAYERED_VERTICAL, CouplingType.THREE_D_CONFRONTATION, CouplingType.BRIDGE),
            "附子底层温阳托举，茯苓白术表层利水疏导，白芍阻尼防温燥伤阴")
    )

    fun findByName(name: String): Formula? = allFormulas.find { it.name == name }

    fun search(query: String): List<Formula> =
        allFormulas.filter { it.name.contains(query) || it.pinyin.contains(query, ignoreCase = true) }
}
