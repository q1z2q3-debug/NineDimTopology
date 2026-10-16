package com.ninedim.topology.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.ninedim.core.ConstitutionType
import com.ninedim.topology.ui.theme.Ink
import com.ninedim.topology.ui.theme.Ochre
import com.ninedim.topology.ui.theme.Paper
import com.ninedim.topology.ui.theme.Pine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstitutionScreen(navController: NavController) {
    var selected by remember { mutableStateOf<ConstitutionType?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("体质辨识", fontWeight = FontWeight.Bold) },
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
        if (selected == null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text("九种体质基底向量", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Ink)
                    Text("每种体质对应一个固定的九维向量偏移，作为病机分析的基底", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                }
                items(ConstitutionType.all()) { type ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        onClick = { selected = type }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = if (type == ConstitutionType.PING_HE) Pine.copy(alpha = 0.15f) else Ochre.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    type.displayName.take(1),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (type == ConstitutionType.PING_HE) Pine else Ochre,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(type.displayName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                                Text(type.description, fontSize = 12.sp, color = Color.Gray, maxLines = 2)
                            }
                            Text("α=${type.alpha}", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }
            }
        } else {
            val type = selected!!
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Ink)) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(type.displayName, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(type.description, color = Color(0xFFC0B8A8), fontSize = 13.sp, lineHeight = 20.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("基底偏移系数 α = ${type.alpha}", color = Ochre, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("九维向量偏移", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                            Spacer(modifier = Modifier.height(12.dp))
                            val vals = type.vector.toArray()
                            com.ninedim.core.NineDimVector.DIMENSION_NAMES.forEachIndexed { i, name ->
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 3.dp)) {
                                    Text("D${i+1} $name", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.width(70.dp))
                                    LinearProgressIndicator(
                                        progress = ((vals[i] + 3) / 6f).toFloat().coerceIn(0f, 1f),
                                        modifier = Modifier.weight(1f).height(6.dp),
                                        color = if (vals[i] >= 0) Ochre else Pine,
                                        trackColor = Color(0xFFE8DFC8)
                                    )
                                    Text("%.1f".format(vals[i]), fontSize = 11.sp, color = Ink, modifier = Modifier.width(35.dp))
                                }
                            }
                        }
                    }
                }

                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("调理方向", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                            Spacer(modifier = Modifier.height(8.dp))
                            val dominant = type.vector.dominantDimension()
                            val dominantName = com.ninedim.core.NineDimVector.DIMENSION_NAMES[dominant]
                            Text("该体质以 $dominantName 维度（D${dominant+1}）偏移最为显著，调理应针对该维度进行反向校正，同时兼顾其他失衡维度。", fontSize = 13.sp, color = Color(0xFF5B544B), lineHeight = 22.sp)
                        }
                    }
                }

                item {
                    OutlinedButton(onClick = { selected = null }, modifier = Modifier.fillMaxWidth()) {
                        Text("返回体质列表")
                    }
                }
            }
        }
    }
}
