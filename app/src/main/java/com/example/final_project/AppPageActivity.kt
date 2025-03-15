package com.example.final_project

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.final_project.ui.theme.Final_projectTheme

class AppPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent {
                Final_projectTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ByteDanceProductsScreen()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // 出错时返回主页面
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

data class Category(val name: String, val isActive: Boolean = false)

data class Product(
    val name: String,
    val logo: Int,
    val category: String,
    val categoryColor: Color,
    val slogan: String,
    val description: String,
    val downloads: String,
    val rating: String,
    val buttonColor: Color
)

@Preview(showBackground = true)
@Composable
fun ByteDanceProductsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 内容部分
        ByteDanceProductsContent()
    }
}

// 提取出内容部分作为可复用组件
@Composable
fun ByteDanceProductsContent() {
    
    val products = remember {
        listOf(
            Product(
                name = "抖音",
                logo = R.drawable.ic_tiktok,
                category = "短视频",
                categoryColor = Color(0xFFF44336),
                slogan = "记录美好生活",
                description = "抖音是一款短视频分享社交软件，用户可以通过这款软件选择歌曲，拍摄音乐短视频，形成自己的作品。",
                downloads = "10亿+",
                rating = "4.8",
                buttonColor = Color(0xFFF44336)
            ),
            Product(
                name = "今日头条",
                logo = R.drawable.ic_toutiao,
                category = "资讯",
                categoryColor = Color(0xFF2196F3),
                slogan = "你关心的，才是头条",
                description = "今日头条是一款基于数据挖掘的推荐引擎产品，它为用户提供个性化、定制化的新闻资讯服务。",
                downloads = "7亿+",
                rating = "4.6",
                buttonColor = Color(0xFF2196F3)
            ),
            Product(
                name = "西瓜视频",
                logo = R.drawable.ic_xigua,
                category = "视频",
                categoryColor = Color(0xFF4CAF50),
                slogan = "高品质视频内容平台",
                description = "西瓜视频是一个中长视频平台，提供高清、流畅的视频观看体验，内容覆盖影视、动漫、纪录片等多个领域。",
                downloads = "5亿+",
                rating = "4.7",
                buttonColor = Color(0xFF4CAF50)
            ),
            Product(
                name = "飞书",
                logo = R.drawable.ic_feishu,
                category = "企业服务",
                categoryColor = Color(0xFF9C27B0),
                slogan = "先进企业协作与管理平台",
                description = "飞书是一款集成了即时通讯、日历、云文档、云盘等功能的企业办公协作平台，旨在提升企业协作效率。",
                downloads = "1亿+",
                rating = "4.5",
                buttonColor = Color(0xFF9C27B0)
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部导航
        Row(
            modifier = Modifier
                .fillMaxWidth()
                ,
            verticalAlignment = Alignment.CenterVertically
        ) {
        }

        // 产品列表
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                ProductCard(product = product)
            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 产品标题和Logo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = androidx.compose.ui.res.painterResource(id = product.logo),
                    contentDescription = "${product.name} Logo",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = product.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(product.categoryColor.copy(alpha = 0.1f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = product.category,
                                color = product.categoryColor,
                                fontSize = 12.sp
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = product.slogan,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 产品描述
            Text(
                text = product.description,
                color = Color.DarkGray,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 下载量、评分和下载按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "下载量",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = product.downloads,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp, end = 12.dp)
                    )
                    
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "评分",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = product.rating,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                
                Button(
                    onClick = { /* 下载应用 */ },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = product.buttonColor
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "下载",
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}