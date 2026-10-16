package com.ninedim.topology.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ninedim.topology.ui.Routes
import com.ninedim.topology.ui.theme.Ink
import com.ninedim.topology.ui.theme.Ochre
import com.ninedim.topology.ui.theme.Paper
import com.ninedim.topology.ui.theme.Pine

data class FeatureItem(
    val title: String,
    val subtitle: String,
    val glyph: String,
    val route: String,
    val color: androidx.compose.ui.graphics.Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val features = listOf(
        FeatureItem("向量计算器", "九维向量合成与可视化", "算", Routes.VECTOR_CALC, Ochre),
        FeatureItem("方剂分析", "经典方剂拓扑结构解析", "方", Routes.FORMULA_ANALYSIS, Pine),
        FeatureItem("逆向推导", "从病机反推候选方剂", "推", Routes.REVERSE_ENGINEER, Ink),
        FeatureItem("体质辨识", "九种体质基底向量建模", "质", Routes.CONSTITUTION, Ochre),
        FeatureItem("关于框架", "九维耦合拓扑框架介绍", "论", Routes.ABOUT, Pine),
        FeatureItem("药物库", "内置30+常用中药数据", "药", Routes.VECTOR_CALC, Ink)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Ink)
                .padding(24.dp)
        ) {
            Text(
                text = "九维耦合拓扑",
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "中医方剂配伍结构描述语言 v1.0",
                color = androidx.compose.ui.graphics.Color(0xFFC0B8A8),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                StatChip("9 维度", Ochre)
                Spacer(modifier = Modifier.width(8.dp))
                StatChip("10 拓扑", Pine)
                Spacer(modifier = Modifier.width(8.dp))
                StatChip("12 方剂", Ochre)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(features) { feature ->
                FeatureCard(feature = feature) {
                    navController.navigate(feature.route)
                }
            }
        }
    }
}

@Composable
fun StatChip(text: String, color: androidx.compose.ui.graphics.Color) {
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            color = androidx.compose.ui.graphics.Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun FeatureCard(feature: FeatureItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Surface(
                color = feature.color.copy(alpha = 0.12f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = feature.glyph,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = feature.color,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = feature.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            Text(
                text = feature.subtitle,
                fontSize = 11.sp,
                color = androidx.compose.ui.graphics.Color(0xFF7A7268),
                maxLines = 1
            )
        }
    }
}
