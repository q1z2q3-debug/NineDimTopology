package com.ninedim.topology.data

import com.ninedim.topology.core.NineDimVector

/**
 * 药物数据类
 */
data class Herb(
    val name: String,
    val pinyin: String,
    val nature: String, // 四气：寒/热/温/凉/平
    val taste: String,  // 五味：辛/甘/酸/苦/咸/淡
    val meridians: List<String>, // 归经
    val vector: NineDimVector,
    val effects: String,
    val indications: String
)

/**
 * 内置药物数据库
 */
object HerbDatabase {
    val allHerbs = listOf(
        Herb("麻黄", "Mahuang", "温", "辛微苦", listOf("肺", "膀胱"),
            NineDimVector(d2 = 1.0, d3 = -1.0, d4 = 3.0, d6 = 1.0, d7 = 3.0),
            "发汗解表，宣肺平喘，利水消肿", "风寒感冒，胸闷喘咳，风水浮肿"),
        Herb("桂枝", "Guizhi", "温", "辛甘", listOf("心", "肺", "膀胱"),
            NineDimVector(d2 = 1.0, d4 = 2.0, d6 = 1.0, d7 = 2.0, d8 = 0.5),
            "发汗解肌，温通经脉，助阳化气", "风寒感冒，脘腹冷痛，血寒经闭"),
        Herb("柴胡", "Chaihu", "平", "苦辛", listOf("肝", "胆", "三焦"),
            NineDimVector(d4 = 1.0, d6 = 2.0, d7 = 2.0),
            "和解表里，疏肝升阳", "感冒发热，寒热往来，胸胁胀痛"),
        Herb("黄连", "Huanglian", "寒", "苦", listOf("心", "脾", "胃", "肝", "胆", "大肠"),
            NineDimVector(d2 = -3.0, d3 = -1.0, d4 = -1.0, d6 = 1.0, d8 = 0.5),
            "清热燥湿，泻火解毒", "湿热痞满，呕吐吞酸，泻痢"),
        Herb("黄芩", "Huangqin", "寒", "苦", listOf("肺", "胆", "脾", "大肠", "小肠"),
            NineDimVector(d2 = -2.0, d3 = -1.0, d4 = -1.0, d6 = 1.0),
            "清热燥湿，泻火解毒，止血", "湿温、暑湿，胸闷呕恶，湿热痞满"),
        Herb("附子", "Fuzi", "大热", "辛甘", listOf("心", "肾", "脾"),
            NineDimVector(d1 = 1.0, d2 = 3.0, d3 = -1.0, d4 = 2.0, d6 = 1.0, d7 = 3.0),
            "回阳救逆，补火助阳，散寒止痛", "亡阳虚脱，肢冷脉微，心阳不足"),
        Herb("干姜", "Ganjiang", "热", "辛", listOf("脾", "胃", "肾", "心", "肺"),
            NineDimVector(d1 = 0.5, d2 = 2.0, d3 = -1.0, d4 = 1.0, d5 = 0.5, d7 = -1.0),
            "温中散寒，回阳通脉，温肺化饮", "脘腹冷痛，呕吐泄泻，肢冷脉微"),
        Herb("人参", "Renshen", "平", "甘微苦", listOf("脾", "肺", "心"),
            NineDimVector(d1 = 3.0, d3 = 1.0, d5 = 1.0, d6 = 1.0, d7 = 0.5, d8 = 2.0),
            "大补元气，复脉固脱，补脾益肺", "体虚欲脱，肢冷脉微，脾虚食少"),
        Herb("黄芪", "Huangqi", "微温", "甘", listOf("肺", "脾"),
            NineDimVector(d1 = 3.0, d2 = 0.5, d6 = 1.0, d7 = -0.5),
            "补气升阳，固表止汗，利水消肿", "气虚乏力，食少便溏，中气下陷"),
        Herb("白术", "Baizhu", "温", "苦甘", listOf("脾", "胃"),
            NineDimVector(d1 = 2.0, d2 = 0.5, d3 = -1.0, d5 = 1.0, d6 = 1.0, d7 = 0.5),
            "健脾益气，燥湿利水，止汗", "脾虚食少，腹胀泄泻，痰饮眩悸"),
        Herb("茯苓", "Fuling", "平", "甘淡", listOf("心", "肺", "脾", "肾"),
            NineDimVector(d1 = 1.0, d3 = -1.0, d4 = 1.0, d5 = -1.0, d6 = 1.0, d8 = 1.0),
            "利水渗湿，健脾，宁心", "水肿尿少，痰饮眩悸，脾虚食少"),
        Herb("甘草", "Gancao", "平", "甘", listOf("心", "肺", "脾", "胃"),
            NineDimVector(d1 = 2.0, d3 = 1.0, d5 = 1.0, d6 = 1.0, d8 = 3.0),
            "补脾益气，清热解毒，祛痰止咳，调和诸药", "脾胃虚弱，倦怠乏力，心悸气短"),
        Herb("芍药", "Shaoyao", "微寒", "苦酸", listOf("肝", "脾"),
            NineDimVector(d1 = 1.0, d2 = -0.5, d3 = 1.0, d5 = 3.0, d6 = 1.0, d7 = -2.0, d8 = 0.5),
            "养血调经，敛阴止汗，柔肝止痛", "血虚萎黄，月经不调，自汗盗汗"),
        Herb("当归", "Danggui", "温", "甘辛", listOf("肝", "心", "脾"),
            NineDimVector(d1 = 2.0, d2 = 1.0, d5 = -1.0, d6 = 1.0, d7 = 1.0),
            "补血活血，调经止痛，润肠通便", "血虚萎黄，眩晕心悸，月经不调"),
        Herb("半夏", "Banxia", "温", "辛", listOf("脾", "胃", "肺"),
            NineDimVector(d2 = 0.5, d3 = -1.0, d4 = 1.0, d5 = 1.0, d6 = 1.0, d7 = -1.0),
            "燥湿化痰，降逆止呕，消痞散结", "湿痰寒痰，咳喘痰多，痰饮眩悸"),
        Herb("五味子", "Wuweizi", "温", "酸甘", listOf("肺", "心", "肾"),
            NineDimVector(d1 = 2.0, d2 = 0.5, d3 = 1.0, d4 = -1.0, d5 = 3.0, d6 = 1.0, d7 = -2.0),
            "收敛固涩，益气生津，补肾宁心", "久嗽虚喘，梦遗滑精，自汗盗汗"),
        Herb("细辛", "Xixin", "温", "辛", listOf("心", "肺", "肾"),
            NineDimVector(d2 = 1.0, d3 = -1.0, d4 = 2.0, d6 = 1.0, d7 = 0.0),
            "解表散寒，祛风止痛，通窍，温肺化饮", "风寒感冒，头痛，牙痛，鼻塞鼻渊"),
        Herb("石膏", "Shigao", "大寒", "辛甘", listOf("肺", "胃"),
            NineDimVector(d1 = 0.5, d2 = -3.0, d4 = 1.0, d6 = 1.0, d7 = 0.0),
            "清热泻火，除烦止渴", "外感热病，高热烦渴，肺热喘咳"),
        Herb("大黄", "Dahuang", "寒", "苦", listOf("脾", "胃", "大肠", "肝", "心包"),
            NineDimVector(d1 = -2.0, d2 = -1.0, d3 = 1.0, d4 = 1.0, d5 = -2.0, d6 = 1.0, d7 = -2.0),
            "泻下攻积，清热泻火，凉血解毒", "实热积滞便秘，血热吐衄，目赤咽肿"),
        Herb("枳实", "Zhishi", "微寒", "苦辛酸", listOf("脾", "胃"),
            NineDimVector(d1 = -1.0, d2 = -0.5, d4 = -1.0, d5 = -1.0, d6 = 1.0, d7 = -2.0),
            "破气消积，化痰散痞", "积滞内停，痞满胀痛，泻痢后重"),
        Herb("厚朴", "Houpo", "温", "苦辛", listOf("脾", "胃", "肺", "大肠"),
            NineDimVector(d2 = 0.5, d3 = -1.0, d4 = 1.0, d5 = -1.0, d6 = 1.0, d7 = -1.0),
            "燥湿消痰，下气除满", "湿滞伤中，脘痞吐泻，食积气滞"),
        Herb("芒硝", "Mangxiao", "寒", "咸苦", listOf("胃", "大肠"),
            NineDimVector(d1 = 2.0, d2 = -1.0, d3 = 1.0, d5 = -2.0, d6 = -1.0),
            "泻下通便，润燥软坚，清火消肿", "实热积滞，腹满胀痛，大便燥结"),
        Herb("熟地黄", "Shudihuang", "微温", "甘", listOf("肝", "肾"),
            NineDimVector(d1 = 3.0, d2 = 0.5, d3 = 1.0, d5 = 2.0, d6 = 1.0),
            "补血滋阴，益精填髓", "血虚萎黄，心悸怔忡，月经不调"),
        Herb("山茱萸", "Shanzhuyu", "微温", "酸涩", listOf("肝", "肾"),
            NineDimVector(d1 = 2.0, d2 = 0.5, d4 = -1.0, d5 = 2.0, d6 = 1.0),
            "补益肝肾，收涩固脱", "眩晕耳鸣，腰膝酸痛，阳痿遗精"),
        Herb("山药", "Shanyao", "平", "甘", listOf("脾", "肺", "肾"),
            NineDimVector(d1 = 2.0, d3 = 1.0, d5 = 1.0, d6 = 1.0),
            "补脾养胃，生津益肺，补肾涩精", "脾虚食少，久泻不止，肺虚喘咳"),
        Herb("泽泻", "Zexie", "寒", "甘淡", listOf("肾", "膀胱"),
            NineDimVector(d1 = -1.0, d2 = -0.5, d3 = -1.0, d4 = 1.0, d5 = -1.0, d6 = 1.0, d7 = -1.0),
            "利水渗湿，泄热，化浊降脂", "小便不利，水肿胀满，泄泻尿少"),
        Herb("牡丹皮", "Mudanpi", "微寒", "苦辛", listOf("心", "肝", "肾"),
            NineDimVector(d2 = -1.0, d4 = 1.0, d5 = -1.0, d6 = 1.0, d7 = 1.0),
            "清热凉血，活血化瘀", "热入营血，温毒发斑，吐血衄血"),
        Herb("阿胶", "Ejiao", "平", "甘", listOf("肺", "肝", "肾"),
            NineDimVector(d1 = 3.0, d3 = 2.0, d5 = 2.0, d6 = 1.0),
            "补血滋阴，润燥，止血", "血虚萎黄，眩晕心悸，肌痿无力"),
        Herb("麦冬", "Maidong", "微寒", "甘微苦", listOf("心", "肺", "胃"),
            NineDimVector(d1 = 2.0, d2 = -0.5, d3 = 2.0, d4 = -1.0, d6 = 1.0),
            "养阴生津，润肺清心", "肺燥干咳，阴虚痨嗽，喉痹咽痛"),
        Herb("生姜", "Shengjiang", "微温", "辛", listOf("肺", "脾", "胃"),
            NineDimVector(d2 = 0.5, d3 = -0.5, d4 = 2.0, d6 = 1.0, d7 = 2.0),
            "解表散寒，温中止呕，化痰止咳", "风寒感冒，胃寒呕吐，寒痰咳嗽"),
        Herb("大枣", "Dazao", "温", "甘", listOf("脾", "胃", "心"),
            NineDimVector(d1 = 2.0, d2 = 0.5, d3 = 1.0, d5 = 1.0, d6 = 1.0, d8 = 1.0),
            "补中益气，养血安神", "脾虚食少，乏力便溏，妇人脏躁")
    )

    fun findByName(name: String): Herb? = allHerbs.find { it.name == name }

    fun search(query: String): List<Herb> =
        allHerbs.filter { it.name.contains(query) || it.pinyin.contains(query, ignoreCase = true) }
}
