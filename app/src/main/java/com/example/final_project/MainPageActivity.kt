package com.example.final_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.final_project.ui.theme.Final_projectTheme
import com.example.final_project.navigation.BottomNavigationBar

class MainPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Final_projectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainPage()
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MainPage() {
    val context = LocalContext.current
    var currentTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = {
                    currentTab = it
                    // 当选择"聊天"选项卡（索引为1）时，跳转到ChatPageActivity
//                    if (it == 1) {
//                        val intent = Intent(context, ChatPageActivity::class.java)
//                        context.startActivity(intent)
//                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentTab) {
                0 -> CompanyIntroductionScreen()
                1 -> ChatPageScreen()
                2 -> ByteDanceProductsScreen()
                3 -> ProfileScreen()
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    // 个人中心页面内容
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("个人中心")
        // 这里可以根据Main_page.html的内容添加更多组件
    }
}