package com.example.final_project

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.final_project.ui.theme.Final_projectTheme
import com.google.gson.GsonBuilder
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.net.SocketTimeoutException
import java.net.UnknownHostException

// 修改为ChatMessage以避免与FastGPTApi中的Message冲突
data class ChatMessage(
    val content: String,
    val isFromUser: Boolean,
    val timestamp: String = "",
    val features: List<String> = emptyList(),
    val isLoading: Boolean = false
)

class ChatPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Final_projectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatPageScreen()
                }
            }
        }
    }
}

@Composable
fun ChatPageScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // 生成一个唯一的chatId
//    val chatId = remember { UUID.randomUUID().toString() }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("您好！我是AI智能助手，很高兴为您服务。请问有什么可以帮助您的吗？", false)
        )
    }

    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // 获取北京时间
    val beijingTime = remember {
        val beijingZone = ZoneId.of("Asia/Shanghai")
        val now = LocalDateTime.now(beijingZone)
        val formatter = DateTimeFormatter.ofPattern("今天 HH:mm")
        now.format(formatter)
    }

    // 发送消息到 FastGPT API 的函数
    // 发送消息到 FastGPT API 的函数
    fun sendMessageToFastGPT(message: String) {
        if (message.isBlank()) return

        // 添加用户消息
        messages.add(ChatMessage(message, true))

        // 添加一个AI消息占位符
        val aiMessageIndex = messages.size
        messages.add(ChatMessage("", false, isLoading = true))

        // 滚动到底部
        coroutineScope.launch {
            listState.animateScrollToItem(messages.size - 1)
        }

        // 调用 API
        coroutineScope.launch {
            try {
                isLoading = true

                // 构建FastGPT请求
                val fastGPTMessage = Message("user", message)

                val request = FastGPTRequest(
                    chatId = "",
                    stream = true,
                    detail = false,
                    responseChatItemId = "ByteDance_info_bot",
                    variables = mapOf(
                        "uid" to "asdfadsfasfd2323",
                        "name" to "张三"
                    ),
                    messages = listOf(fastGPTMessage)
                )

                // 输出请求JSON到日志
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonRequest = gson.toJson(request)
                Log.d("FastGPT_Request", jsonRequest)

                // 使用流式API
                FastGPTApi.chatWithFastGPTStream(request).collect { content ->
                    // 更新AI消息内容
                    if (aiMessageIndex < messages.size) {
                        val currentMessage = messages[aiMessageIndex]
                        messages[aiMessageIndex] = currentMessage.copy(
                            content = currentMessage.content + content,
                            isLoading = false
                        )

                        // 滚动到底部
                        listState.animateScrollToItem(messages.size - 1)
                    }
                }

            } catch (e: Exception) {
                Log.e("ChatPageActivity", "流式API调用失败", e)

                // 如果出错，显示错误消息
                if (aiMessageIndex < messages.size) {
                    messages[aiMessageIndex] =
                        ChatMessage("抱歉，连接服务器时出现问题: ${e.message}", false)
                }
            } catch (e: Exception) {
                Log.e("ChatPageActivity", "API调用失败", e)

                // 如果出错，显示更友好的错误消息
                if (aiMessageIndex < messages.size) {
                    val errorMessage = when {
                        e is SocketTimeoutException ->
                            "连接服务器超时。可能的原因：\n1. 网络连接不稳定\n2. API服务器响应时间过长\n3. API端点可能不正确\n\n建议：\n- 检查网络连接\n- 确认API密钥和端点是否正确"

                        e is UnknownHostException ->
                            "无法连接到服务器。可能的原因：\n1. 网络连接问题\n2. API端点域名不正确\n\n建议：\n- 检查网络连接\n- 确认API端点是否正确"

                        else -> "连接服务器时出现问题: ${e.message}"
                    }
                    messages[aiMessageIndex] = ChatMessage(errorMessage, false, isLoading = false)
                }
            } finally {
                isLoading = false
            }
        }
    }

    // 其余UI代码保持不变
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 聊天时间标记
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = beijingTime,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
                    .background(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        // 聊天消息列表
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFF2F2F7))
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(messages) { message ->
                if (message.isLoading) {
                    LoadingMessageBubble()
                } else {
                    MessageBubble(message = message)
                }
            }
        }

        // 底部输入框
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("输入消息...") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF2F2F7),
                        unfocusedContainerColor = Color(0xFFF2F2F7),
                        disabledContainerColor = Color(0xFFF2F2F7),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp),
                    enabled = !isLoading
                )

                IconButton(
                    onClick = {
                        if (inputText.isNotEmpty() && !isLoading) {
                            val message = inputText.trim()
                            inputText = ""
                            sendMessageToFastGPT(message)
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isLoading) Color.Gray else Color(0xFF3B82F6)),
                    enabled = !isLoading
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "发送",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingMessageBubble() {
    // 保持不变
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        // AI头像
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFDCEAFF))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = "AI头像",
                tint = Color(0xFF3B82F6)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 加载动画
        Surface(
            shape = RoundedCornerShape(
                topStart = 4.dp,
                topEnd = 18.dp,
                bottomStart = 18.dp,
                bottomEnd = 18.dp
            ),
            color = Color(0xFFF3F4F6)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                LoadingDot(0)
                LoadingDot(150)
                LoadingDot(300)
            }
        }
    }
}

@Composable
fun LoadingDot(delayMillis: Int) {
    // 保持不变
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val size by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, delayMillis),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot size"
    )

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(Color(0xFF3B82F6))
    )
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
    ) {
        if (message.isFromUser) {
            // 用户消息
            Surface(
                shape = RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = 18.dp,
                    bottomEnd = 4.dp
                ),
                color = Color(0xFF3B82F6)
            ) {
                Text(
                    text = message.content,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
        } else {
            // AI消息
            Row(verticalAlignment = Alignment.Top) {
                // AI头像
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDCEAFF))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "AI头像",
                        tint = Color(0xFF3B82F6)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // 消息内容
                Surface(
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 18.dp,
                        bottomStart = 18.dp,
                        bottomEnd = 18.dp
                    ),
                    color = Color(0xFFF3F4F6)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        MarkdownText(markdown = message.content)
                        if (message.features.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            message.features.forEachIndexed { index, feature ->
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${index + 1}. ",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = feature)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
