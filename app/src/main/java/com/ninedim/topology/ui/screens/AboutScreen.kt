package com.ninedim.topology.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ninedim.core.CouplingType
import com.ninedim.core.NineDimVector
import com.ninedim.topology.ui.theme.Ink
import com.ninedim.topology.ui.theme.Ochre
import com.ninedim.topology.ui.theme.Paper
import com.ninedim.topology.ui.theme.Pine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("关于框架", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Paper)
            )
        },
        containerColor = Paper
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Ink)) {
                    androidx.compose.foundation.layout.Column(modifier = Modifier.padding(24.dp)) {
                        Text("九维耦合拓扑框架", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Nine-Dimensional Coupling Topology Framework", color = Color(0xFFC0B8A8), fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("一种用于描述中医方剂配伍结构的数学语言，将每味药物抽象为九维药效向量，将方剂配伍抽象为向量空间中的耦合拓扑结构。", color = Color(0xFFD0C8B8), fontSize = 13.sp, lineHeight = 22.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("版本 v1.0.0", color = Ochre, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    androidx.compose.foundation.layout.Column(modifier = Modifier.padding(16.dp)) {
                        Text("九维向量定义", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                        Spacer(modifier = Modifier.height(8.dp))
                        NineDimVector.DIMENSION_NAMES.forEachIndexed { i, name ->
                            Row(modifier = Modifier.padding(vertical = 3.dp)) {
                                Text("D${i+1}", fontSize = 12.sp, color = Ochre, fontWeight = FontWeight.Bold, modifier = Modifier.width(35.dp))
                                Text(name, fontSize = 13.sp, color = Ink, modifier = Modifier.width(50.dp))
                                Text(getDimensionDesc(i), fontSize = 12.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    androidx.compose.foundation.layout.Column(modifier = Modifier.padding(16.dp)) {
                        Text("十种耦合拓扑", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                        Spacer(modifier = Modifier.height(8.dp))
                        CouplingType.all().forEach { type ->
                            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, modifier = Modifier.padding(vertical = 5.dp)) {
                                Surface(color = Pine.copy(alpha = 0.12f), shape = RoundedCornerShape(8.dp)) {
                                    Text(type.displayName, fontSize = 11.sp, color = Pine, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(type.representativeFormula, fontSize = 11.sp, color = Ochre, modifier = Modifier.width(60.dp))
                                Text(type.description, fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    androidx.compose.foundation.layout.Column(modifier = Modifier.padding(16.dp)) {
                        Text("核心公式", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("向量合成：", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
                        Text("V_j = Σ(w_i × v_ij) + Σ(κ_ik × w_i × w_k × (v_ij ⊗ v_kj))", fontSize = 12.sp, color = Ochre, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("其中 w_i 为剂量权重，v_ij 为第 i 味药在第 j 维的分量，κ_ik 为药对相互作用系数（七情），⊗ 为哈达玛积。", fontSize = 12.sp, color = Color.Gray, lineHeight = 20.sp)
                    }
                }
            }

            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    androidx.compose.foundation.layout.Column(modifier = Modifier.padding(16.dp)) {
                        Text("数据规模", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            StatBox("33", "味中药", Ochre, Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(12.dp))
                            StatBox("12", "首经典方剂", Pine, Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(12.dp))
                            StatBox("9", "种体质模型", Ink, Modifier.weight(1f))
                        }
                    }
                }
            }

            item {
                Text("本框架为理论研究工具，不构成医疗建议。临床用药请遵医嘱。", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
fun StatBox(number: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(number, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 11.sp, color = Color.Gray)
        }
    }
}

fun getDimensionDesc(index: Int): String = when (index) {
    0 -> "补益滋养(+) / 泻下耗散(-)"
    1 -> "温热(+) / 寒凉(-)"
    2 -> "生津(+) / 燥湿(-)"
    3 -> "发散宣通(+) / 内敛收敛(-)"
    4 -> "收敛固涩(+) / 消散破结(-)"
    5 -> "归经定位"
    6 -> "升浮向外(+) / 沉降向内(-)"
    7 -> "信息整合"
    8 -> "周期调节"
    else -> ""
}
