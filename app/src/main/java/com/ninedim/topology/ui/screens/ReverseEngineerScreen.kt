package com.ninedim.topology.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ninedim.topology.core.ConstitutionType
import com.ninedim.topology.core.NineDimVector
import com.ninedim.topology.core.ReverseEngineer
import com.ninedim.topology.ui.theme.Ink
import com.ninedim.topology.ui.theme.Ochre
import com.ninedim.topology.ui.theme.Paper
import com.ninedim.topology.ui.theme.Pine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReverseEngineerScreen(navController: NavController) {
    var d1 by remember { mutableStateOf(0f) }
    var d2 by remember { mutableStateOf(0f) }
    var d3 by remember { mutableStateOf(0f) }
    var d4 by remember { mutableStateOf(0f) }
    var d5 by remember { mutableStateOf(0f) }
    var d6 by remember { mutableStateOf(0f) }
    var d7 by remember { mutableStateOf(0f) }
    var d8 by remember { mutableStateOf(0f) }
    var d9 by remember { mutableStateOf(0f) }
    var selectedConstitution by remember { mutableStateOf(ConstitutionType.PING_HE) }
    var result by remember { mutableStateOf<ReverseEngineer.ReverseResult?>(null) }

    val sliders = listOf(
        "D1 容纳" to d1, "D2 温度" to d2, "D3 润燥" to d3,
        "D4 表达" to d4, "D5 固化" to d5, "D6 结构" to d6,
        "D7 运动" to d7, "D8 调和" to d8, "D9 节律" to d9
    )
    val setters = listOf<(Float) -> Unit>(
        { d1 = it }, { d2 = it }, { d3 = it }, { d4 = it }, { d5 = it },
        { d6 = it }, { d7 = it }, { d8 = it }, { d9 = it }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("逆向推导", fontWeight = FontWeight.Bold) },
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("输入病机向量", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("滑动调节各维度失衡程度（-5 ~ +5）", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(12.dp))
                        sliders.forEachIndexed { index, (label, value) ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(label, fontSize = 11.sp, color = Ink, modifier = Modifier.width(65.dp))
                                Slider(
                                    value = value,
                                    onValueChange = setters[index],
                                    valueRange = -5f..5f,
                                    modifier = Modifier.weight(1f),
                                    colors = SliderDefaults.colors(
                                        thumbColor = Ochre,
                                        activeTrackColor = Ochre
                                    )
                                )
                                Text("%.1f".format(value), fontSize = 11.sp, color = Ochre, modifier = Modifier.width(35.dp))
                            }
                        }
                    }
                }
            }

            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("体质基底", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                        Spacer(modifier = Modifier.height(8.dp))
                        var expanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                            OutlinedTextField(
                                value = selectedConstitution.displayName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("选择体质") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.fillMaxWidth().menuAnchor()
                            )
                            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                ConstitutionType.all().forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type.displayName) },
                                        onClick = { selectedConstitution = type; expanded = false }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(selectedConstitution.description, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        val diseaseVec = NineDimVector(d1.toDouble(), d2.toDouble(), d3.toDouble(), d4.toDouble(), d5.toDouble(), d6.toDouble(), d7.toDouble(), d8.toDouble(), d9.toDouble())
                        result = ReverseEngineer.engineer(diseaseVec, selectedConstitution)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Ochre)
                ) {
                    Text("开始逆向推导", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            result?.let { res ->
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Ink)) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("推导结果", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(res.explanation, color = Color(0xFFD0C8B8), fontSize = 13.sp, lineHeight = 22.sp)
                        }
                    }
                }

                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("推荐耦合拓扑", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
                            Spacer(modifier = Modifier.height(8.dp))
                            res.requiredTopologies.forEach { type ->
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                                    Surface(color = Pine.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)) {
                                        Text(type.displayName, fontSize = 12.sp, color = Pine, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(type.description, fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("候选方剂", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
                            Spacer(modifier = Modifier.height(8.dp))
                            res.candidateFormulas.forEach { name ->
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
                                    Surface(color = Ochre.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)) {
                                        Text(name, fontSize = 14.sp, color = Ochre, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("候选药物", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(res.candidateHerbs.joinToString("、"), fontSize = 13.sp, color = Color(0xFF5B544B))
                        }
                    }
                }
            }
        }
    }
}
