package com.ninedim.topology.ui.screens

import androidx.compose.foundation.background
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
import com.ninedim.core.data.FormulaDatabase
import com.ninedim.topology.ui.theme.Ink
import com.ninedim.topology.ui.theme.Ochre
import com.ninedim.topology.ui.theme.Paper
import com.ninedim.topology.ui.theme.Pine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormulaAnalysisScreen(
    navController: NavController,
    initialFormula: String = ""
) {
    var selectedFormulaName by remember { mutableStateOf(initialFormula) }
    val selectedFormula = remember(selectedFormulaName) {
        FormulaDatabase.findByName(selectedFormulaName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("方剂分析", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (selectedFormula == null) {
                // Formula list
                Text(
                    "选择方剂进行拓扑分析",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Ink
                )
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(FormulaDatabase.allFormulas) { formula ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(formula.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Ink, modifier = Modifier.weight(1f))
                                    Text(formula.source, fontSize = 11.sp, color = Color.Gray)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(formula.effects, fontSize = 13.sp, color = Ochre, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("组成：${formula.composition.joinToString("、") { it.first }}", fontSize = 12.sp, color = Color(0xFF7A7268))
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    formula.couplingTypes.take(4).forEach { type ->
                                        Surface(
                                            color = Pine.copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.padding(end = 6.dp)
                                        ) {
                                            Text(
                                                type.displayName,
                                                fontSize = 10.sp,
                                                color = Pine,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { selectedFormulaName = formula.name },
                                    colors = ButtonDefaults.buttonColors(containerColor = Ochre)
                                ) {
                                    Text("查看分析", fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            } else {
                // Formula detail
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Card(colors = CardDefaults.cardColors(containerColor = Ink)) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(selectedFormula.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                Text(selectedFormula.source, color = Color(0xFFC0B8A8), fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(selectedFormula.effects, color = Ochre, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("主治：${selectedFormula.indications}", color = Color(0xFFD0C8B8), fontSize = 13.sp)
                            }
                        }
                    }

                    item {
                        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("药物组成", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                                Spacer(modifier = Modifier.height(8.dp))
                                selectedFormula.composition.forEach { (name, dose) ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(name, fontSize = 14.sp, color = Ink, modifier = Modifier.weight(1f))
                                        Text("${dose}两", fontSize = 13.sp, color = Ochre, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("耦合拓扑类型", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                                Spacer(modifier = Modifier.height(8.dp))
                                selectedFormula.couplingTypes.forEach { type ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            color = Ochre.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(type.displayName, fontSize = 13.sp, color = Ochre, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(type.description, fontSize = 12.sp, color = Color(0xFF7A7268), modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("拓扑结构解析", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(selectedFormula.description, fontSize = 13.sp, color = Color(0xFF5B544B), lineHeight = 22.sp)
                            }
                        }
                    }

                    item {
                        OutlinedButton(
                            onClick = { selectedFormulaName = "" },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("返回方剂列表")
                        }
                    }
                }
            }
        }
    }
}
