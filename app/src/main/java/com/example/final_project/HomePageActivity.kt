package com.example.final_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

import com.example.final_project.ui.theme.Final_projectTheme

class HomePageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Final_projectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompanyIntroductionScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyIntroductionScreen() {
    Scaffold(

    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // 公司卡片
            item {
                CompanyCard()
            }

            // 公司简介
            item {
                Text(
                    text = "公司简介",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
                )
                Text(
                    text = "字节跳动是全球领先的科技公司，成立于2012年，总部位于北京，在上海、深圳、广州、杭州等地设有分支机构，并在全球多个国家和地区开展业务。公司致力于通过创新的技术和产品，丰富人们的生活，提升社会的效率。",
                    color = Color.DarkGray,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            // 核心业务
            item {
                Text(
                    text = "核心业务",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                BusinessCard(
                    icon = R.drawable.ic_robot,
                    iconBgColor = Color(0xFFE6F0FF),
                    iconColor = Color(0xFF007AFF),
                    title = "内容平台",
                    description = "今日头条、抖音、西瓜视频等内容创作与分享平台"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                BusinessCard(
                    icon = R.drawable.ic_chart,
                    iconBgColor = Color(0xFFF3E6FF),
                    iconColor = Color(0xFF9333EA),
                    title = "教育科技",
                    description = "飞书、gogokid等企业协作与在线教育产品"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                BusinessCard(
                    icon = R.drawable.ic_cogs,
                    iconBgColor = Color(0xFFE6FFEE),
                    iconColor = Color(0xFF10B981),
                    title = "全球化业务",
                    description = "TikTok、Lark等面向全球市场的创新应用"
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 企业文化
            item {
                Text(
                    text = "企业文化",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                CompanyCultureGrid()
                
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 公司环境
            item {
                Text(
                    text = "公司环境",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                CompanyEnvironmentGrid()
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun CompanyCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // 公司图片
            Image(
                painter = rememberAsyncImagePainter(
                    model = "https://images.unsplash.com/photo-1582192730841-2a682d7375f9?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=80"
                ),
                contentDescription = "公司大楼",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            
            // 公司信息
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "字节跳动科技有限公司",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "成立于2012年 · 互联网科技领域",
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = "地址",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "北京市海淀区知春路甲5号院",
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_people),
                        contentDescription = "员工数",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "10万+人",
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_globe),
                        contentDescription = "网站",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "www.bytedance.com",
                        color = Color(0xFF007AFF),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BusinessCard(
    icon: Int,
    iconBgColor: Color,
    iconColor: Color,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = description,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun CompanyCultureGrid() {
    val cultureItems = listOf(
        Pair("使命", "激发创造，丰富生活"),
        Pair("愿景", "成为全球创新的科技公司"),
        Pair("价值观", "始终创业、追求极致、开放协作、坦诚清晰"),
        Pair("文化", "多元包容、平等开放")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CultureItem(
                title = cultureItems[0].first,
                description = cultureItems[0].second,
                modifier = Modifier.weight(1f)
            )
            CultureItem(
                title = cultureItems[1].first,
                description = cultureItems[1].second,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CultureItem(
                title = cultureItems[2].first,
                description = cultureItems[2].second,
                modifier = Modifier.weight(1f)
            )
            CultureItem(
                title = cultureItems[3].first,
                description = cultureItems[3].second,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CultureItem(title: String, description: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = description,
                color = Color.DarkGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CompanyEnvironmentGrid() {
    val imageUrls = listOf(
        "https://images.unsplash.com/photo-1604328698692-f76ea9498e76?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=80",
        "https://images.unsplash.com/photo-1497366754035-f200968a6e72?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=80",
        "https://images.unsplash.com/photo-1572025442646-866d16c84a54?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=80",
        "https://images.unsplash.com/photo-1497215728101-856f4ea42174?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=80"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrls[0]),
                contentDescription = "办公环境",
                modifier = Modifier
                    .height(128.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = rememberAsyncImagePainter(model = imageUrls[1]),
                contentDescription = "办公环境",
                modifier = Modifier
                    .height(128.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrls[2]),
                contentDescription = "办公环境",
                modifier = Modifier
                    .height(128.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = rememberAsyncImagePainter(model = imageUrls[3]),
                contentDescription = "办公环境",
                modifier = Modifier
                    .height(128.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyIntroductionPreview() {
    Final_projectTheme {
        CompanyIntroductionScreen()
    }
}