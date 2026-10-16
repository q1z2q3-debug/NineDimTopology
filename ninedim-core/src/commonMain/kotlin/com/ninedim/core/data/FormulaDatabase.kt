package com.ninedim.core.data

import com.ninedim.core.CouplingType
import com.ninedim.core.NineDimVector

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
            "附子底层温阳托举，茯苓白术表层利水疏导，白芍阻尼防温燥伤阴"),

        // 新增经典方剂
        Formula("银翘散", "Yinqiao San", "《温病条辨》",
            listOf("金银花" to 3.0, "连翘" to 3.0, "薄荷" to 2.0, "牛蒡子" to 2.0, "桔梗" to 2.0, "淡竹叶" to 1.5, "荆芥" to 2.0, "淡豆豉" to 2.0, "甘草" to 1.5, "芦根" to 3.0),
            "辛凉透表，清热解毒", "温病初起，发热无汗，头痛口渴，咳嗽咽痛",
            listOf(CouplingType.PARALLEL_HORIZONTAL, CouplingType.STAR, CouplingType.DEFLECTION),
            "银花连翘相须清热解毒为核心，薄荷荆芥宣散透表，甘草星型调和"),

        Formula("桑菊饮", "Sangju Yin", "《温病条辨》",
            listOf("桑叶" to 3.0, "菊花" to 2.0, "杏仁" to 2.0, "薄荷" to 1.5, "桔梗" to 2.0, "甘草" to 1.0, "芦根" to 3.0),
            "疏风清热，宣肺止咳", "风温初起，但咳，身热不甚，口微渴",
            listOf(CouplingType.PARALLEL_HORIZONTAL, CouplingType.LAYERED_VERTICAL),
            "桑叶菊花清散上焦风热，杏仁桔梗宣降肺气，分层治疗"),

        Formula("麻杏石甘汤", "Maxing Shigan Tang", "《伤寒论》",
            listOf("麻黄" to 2.0, "杏仁" to 2.0, "石膏" to 4.0, "甘草" to 1.5),
            "辛凉宣泄，清肺平喘", "外感风邪，邪热壅肺证，身热不解，咳逆气急",
            listOf(CouplingType.THREE_D_CONFRONTATION, CouplingType.DEFLECTION),
            "麻黄宣肺平喘+石膏清泄肺热，寒热对立体；石膏倍麻黄，宣而不燥"),

        Formula("大柴胡汤", "Dachaihu Tang", "《伤寒论》",
            listOf("柴胡" to 3.0, "黄芩" to 2.0, "芍药" to 2.0, "半夏" to 2.0, "枳实" to 2.0, "大黄" to 1.0, "生姜" to 3.0, "大枣" to 2.0),
            "和解少阳，内泻热结", "少阳阳明合病，往来寒热，胸胁苦满，呕不止",
            listOf(CouplingType.LAYERED_VERTICAL, CouplingType.BRIDGE, CouplingType.TEMPORAL),
            "柴胡黄芩和解少阳（外层），大黄枳实泻阳明热结（内层），半夏桥接"),

        Formula("四物汤", "Siwu Tang", "《太平惠民和剂局方》",
            listOf("熟地黄" to 3.0, "当归" to 3.0, "芍药" to 3.0, "川芎" to 1.5),
            "补血调血", "营血虚滞证，头晕目眩，心悸失眠，月经不调",
            listOf(CouplingType.RING, CouplingType.LAYERED_VERTICAL),
            "熟地当归补血（静），芍药川芎行血（动），四药环环相扣，补而不滞"),

        Formula("补中益气汤", "Buzhong Yiqi Tang", "《脾胃论》",
            listOf("黄芪" to 4.0, "人参" to 2.0, "白术" to 2.0, "炙甘草" to 2.0, "当归" to 2.0, "陈皮" to 1.5, "升麻" to 1.0, "柴胡" to 1.0),
            "补中益气，升阳举陷", "脾虚气陷证，饮食减少，体倦肢软，少气懒言",
            listOf(CouplingType.LAYERED_VERTICAL, CouplingType.DEFLECTION, CouplingType.STAR),
            "黄芪人参白术底层补气，升麻柴胡引清气上行，陈皮防气滞"),

        Formula("玉屏风散", "Yupingfeng San", "《医方类聚》",
            listOf("黄芪" to 3.0, "白术" to 2.0, "防风" to 1.0),
            "益气固表止汗", "表虚自汗，汗出恶风，面色㿠白，易感风邪",
            listOf(CouplingType.LAYERED_VERTICAL, CouplingType.BRIDGE),
            "黄芪益气固表为君，白术健脾益气为臣，防风走表祛风为佐，补散结合"),

        Formula("生脉散", "Shengmai San", "《医学启源》",
            listOf("人参" to 2.0, "麦冬" to 3.0, "五味子" to 1.5),
            "益气生津，敛阴止汗", "温热暑热，耗气伤阴，体倦气短，咽干口渴",
            listOf(CouplingType.TRIANGLE_VECTOR, CouplingType.LAYERED_VERTICAL),
            "人参大补元气（上），麦冬养阴清肺（中），五味子敛肺止汗（下），三矢量合成"),

        Formula("归脾汤", "Guipi Tang", "《济生方》",
            listOf("人参" to 2.0, "黄芪" to 3.0, "白术" to 2.0, "茯苓" to 2.0, "当归" to 2.0, "龙眼肉" to 2.0, "酸枣仁" to 3.0, "远志" to 1.5, "木香" to 1.0, "甘草" to 1.0, "生姜" to 2.0, "大枣" to 2.0),
            "益气补血，健脾养心", "心脾气血两虚证，心悸怔忡，健忘失眠，体倦食少",
            listOf(CouplingType.STAR, CouplingType.RING, CouplingType.LAYERED_VERTICAL),
            "参芪术草健脾益气，当归龙眼养血，酸枣仁远志宁心安神，木香理气"),

        Formula("炙甘草汤", "Zhigancao Tang", "《伤寒论》",
            listOf("炙甘草" to 4.0, "人参" to 2.0, "生地黄" to 4.0, "阿胶" to 2.0, "麦冬" to 3.0, "桂枝" to 2.0, "生姜" to 2.0, "麻仁" to 2.0, "大枣" to 3.0),
            "益气滋阴，通阳复脉", "阴血阳气虚弱，心脉失养证，脉结代，心动悸",
            listOf(CouplingType.STAR, CouplingType.LAYERED_VERTICAL, CouplingType.THREE_D_CONFRONTATION),
            "炙甘草为星型核心，地黄阿胶麦冬滋阴血（静），桂枝生姜温通阳气（动）"),

        Formula("知柏地黄丸", "Zhibai Dihuang Wan", "《医宗金鉴》",
            listOf("熟地黄" to 4.0, "山茱萸" to 2.0, "山药" to 2.0, "泽泻" to 1.5, "牡丹皮" to 1.5, "茯苓" to 1.5, "知母" to 2.0, "黄柏" to 2.0),
            "滋阴降火", "阴虚火旺证，潮热盗汗，口干咽痛，耳鸣遗精",
            listOf(CouplingType.THREE_D_CONFRONTATION, CouplingType.LAYERED_VERTICAL),
            "六味地黄丸滋肾阴为底，加知母黄柏清相火，补泻兼施"),

        Formula("金匮肾气丸", "Jinkui Shenqi Wan", "《金匮要略》",
            listOf("干地黄" to 4.0, "山药" to 2.0, "山茱萸" to 2.0, "泽泻" to 1.5, "茯苓" to 1.5, "牡丹皮" to 1.5, "桂枝" to 1.0, "附子" to 1.0),
            "温补肾阳", "肾阳不足证，腰痛脚软，身半以下常有冷感，少腹拘急",
            listOf(CouplingType.THREE_D_CONFRONTATION, CouplingType.LAYERED_VERTICAL),
            "六味滋肾阴为基础，加少量桂枝附子温补肾阳，阴中求阳"),

        Formula("半夏泻心汤", "Banxia Xiexin Tang", "《伤寒论》",
            listOf("半夏" to 3.0, "黄芩" to 2.0, "干姜" to 2.0, "人参" to 2.0, "炙甘草" to 2.0, "黄连" to 1.0, "大枣" to 2.0),
            "寒热平调，消痞散结", "寒热错杂之痞证，心下痞，但满不痛，呕吐肠鸣下利",
            listOf(CouplingType.THREE_D_CONFRONTATION, CouplingType.CIRCUIT, CouplingType.STAR),
            "半夏干姜辛温开结+黄芩黄连苦寒泄热，寒热双向振荡；人参甘草扶正"),

        Formula("桃核承气汤", "Taohe Chengqi Tang", "《伤寒论》",
            listOf("桃仁" to 3.0, "大黄" to 3.0, "桂枝" to 2.0, "炙甘草" to 2.0, "芒硝" to 2.0),
            "逐瘀泻热", "下焦蓄血证，少腹急结，小便自利，其人如狂",
            listOf(CouplingType.TEMPORAL, CouplingType.THREE_D_CONFRONTATION),
            "桃仁活血化瘀+大黄泻热通便+芒硝软坚，时序接力逐瘀；桂枝通血脉"),

        Formula("血府逐瘀汤", "Xuefu Zhuyu Tang", "《医林改错》",
            listOf("桃仁" to 3.0, "红花" to 2.0, "当归" to 2.0, "生地黄" to 2.0, "川芎" to 1.5, "赤芍" to 2.0, "牛膝" to 2.0, "桔梗" to 1.5, "柴胡" to 1.0, "枳壳" to 1.5, "甘草" to 1.0),
            "活血化瘀，行气止痛", "胸中血瘀证，胸痛头痛日久，痛如针刺，内热烦闷",
            listOf(CouplingType.LAYERED_VERTICAL, CouplingType.BRIDGE, CouplingType.STAR),
            "桃红四物汤活血化瘀（血分），柴胡枳壳桔梗行气（气分），牛膝引血下行")
    )

    fun findByName(name: String): Formula? = allFormulas.find { it.name == name }

    fun search(query: String): List<Formula> =
        allFormulas.filter { it.name.contains(query) || it.pinyin.contains(query, ignoreCase = true) }
}
