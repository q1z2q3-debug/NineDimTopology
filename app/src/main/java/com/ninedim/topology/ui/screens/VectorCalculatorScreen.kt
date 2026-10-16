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
import com.ninedim.topology.core.NineDimVector
import com.ninedim.topology.data.HerbDatabase
import com.ninedim.topology.ui.theme.Ink
import com.ninedim.topology.ui.theme.Ochre
import com.ninedim.topology.ui.theme.Paper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VectorCalculatorScreen(navController: NavController) {
    var selectedHerbs by remember { mutableStateOf<List<Pair<String, Double>>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    val resultVector = remember(selectedHerbs) {
        var result = NineDimVector()
        for ((name, dose) in selectedHerbs) {
            val herb = HerbDatabase.findByName(name)
            if (herb != null) result = result + (herb.vector * dose)
        }
        result
    }

    val filteredHerbs = remember(searchQuery) {
        if (searchQuery.isEmpty()) HerbDatabase.allHerbs.take(20)
        else HerbDatabase.search(searchQuery)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("向量计算器", fontWeight = FontWeight.Bold) },
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
            // Selected herbs
            if (selectedHerbs.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("已选药物 (${selectedHerbs.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                        Spacer(modifier = Modifier.height(8.dp))
                        selectedHerbs.forEachIndexed { index, (name, dose) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("$name", fontSize = 14.sp, color = Ink, modifier = Modifier.weight(1f))
                                Text("${dose}两", fontSize = 13.sp, color = Ochre, fontWeight = FontWeight.Medium)
                                IconButton(onClick = {
                                    selectedHerbs = selectedHerbs.filterIndexed { i, _ -> i != index }
                                }) {
                                    Text("×", color = Color.Red, fontSize = 18.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        // Result vector display
                        Text("合成向量", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Ink)
                        Spacer(modifier = Modifier.height(4.dp))
                        val vals = resultVector.toArray()
                        NineDimVector.DIMENSION_NAMES.forEachIndexed { i, name ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("D${i+1} $name", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.width(70.dp))
                                LinearProgressIndicator(
                                    progress = (vals[i] / 15f).toFloat().coerceIn(0f, 1f),
                                    modifier = Modifier.weight(1f).height(6.dp),
                                    color = if (vals[i] >= 0) Ochre else Color(0xFF5B7B8A),
                                    trackColor = Color(0xFFE8DFC8)
                                )
                                Text("%.1f".format(vals[i]), fontSize = 11.sp, color = Ink, modifier = Modifier.width(40.dp))
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }
                }
            }

            // Search
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("搜索药物") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Herb list
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredHerbs) { herb ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(herb.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
                                Text("${herb.nature} · ${herb.taste} · 归${herb.meridians.joinToString("")}", fontSize = 11.sp, color = Color.Gray)
                                Text(herb.effects, fontSize = 11.sp, color = Color(0xFF7A7268), maxLines = 1)
                            }
                            Button(
                                onClick = {
                                    selectedHerbs = selectedHerbs + (herb.name to 3.0)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Ochre)
                            ) {
                                Text("添加", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
