package com.example.final_project

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.BufferedReader
import java.io.InputStreamReader


// FastGPT 消息数据类
data class Message(
    val role: String,
    val content: String
)

// FastGPT 请求数据类
data class FastGPTRequest(
    val chatId: String? = null,
    val stream: Boolean = false,
    val detail: Boolean = false,
    val responseChatItemId: String? = null,
    val variables: Map<String, String> = emptyMap(),
    val messages: List<Message>
)

data class ChatResponseData(
    val id: String,
    val answer: String,
    val responseData: Map<String, Any>? = null
)

// 流式响应数据类
data class StreamResponse(
    val id: String = "",
    val object_: String = "",
    @SerializedName("created")
    val created: Long = 0,
    val choices: List<Choice> = emptyList()
)

data class Choice(
    val delta: Delta = Delta(),
    val index: Int = 0,
    @SerializedName("finish_reason")
    val finishReason: String? = null
)

data class Delta(
    val content: String = ""
)

// 创建 API 服务实例
// 修改FastGPT API对象的初始化方式
object FastGPTApi {
    //TODO 把它改成主机的IP,主机配置好了
    const val BASE_URL = "https://api.fastgpt.in/api/v1/chat/completions" // 替换为实际的 FastGPT API URL
    const val APP_KEY = "fastgpt-rcWC6jEr8PWK13rPsmoVJBbIqvlzd5p7PTWaDzfsiGGQh9njRs6ByswcL"
//    const val IP="10.26.61.76:3000"
//    const val BASE_URL = "http://"+IP+"/api/v1/chat/completions"
//    const val APP_KEY = "fastgpt-kdGkRWeK1ylkCrQVb2enYu1Ra4C0gmneLnbsLpt2SDR2xRN1pBJTyOFdb"


    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            // 增加超时设置到60秒
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            // 添加重试拦截器
            .addInterceptor { chain ->
                val request = chain.request()
                var response = chain.proceed(request)
                var tryCount = 0
                val maxRetry = 3 // 最多重试3次

                while (!response.isSuccessful && tryCount < maxRetry) {
                    Log.d("FastGPT_API", "Request failed, retrying... (${tryCount + 1}/$maxRetry)")
                    tryCount++
                    response.close()
                    // 重试前等待一段时间
                    Thread.sleep((1000 * tryCount).toLong())
                    response = chain.proceed(request)
                }

                response
            }
            .build()
    }

    // 处理流式响应
    // 修改chatWithFastGPTStream方法，检查API端点
    // 修改chatWithFastGPTStream方法，添加错误处理和重试机制
    fun chatWithFastGPTStream(request: FastGPTRequest): Flow<String> = flow {
        val gson = Gson()
        val jsonRequest = gson.toJson(request)

        // 记录请求JSON
        Log.d("FastGPT_API", "Request JSON: $jsonRequest")

        val requestBody = jsonRequest.toRequestBody("application/json".toMediaType())

        val httpRequest = Request.Builder()
            .url(BASE_URL) // 替换为实际的 FastGPT API URL
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $APP_KEY") // 替换为您的实际API密钥
            .post(requestBody)
            .build()

        var retryCount = 0
        val maxRetries = 3
        var lastException: Exception? = null

        while (retryCount < maxRetries) {
            try {
                val response = client.newCall(httpRequest).execute()

                // 记录响应状态
                Log.d("FastGPT_API", "Response Code: ${response.code}")

                if (!response.isSuccessful) {
                    Log.e("FastGPT_API", "API请求失败: ${response.code}")
                    val errorBody = response.body?.string()
                    Log.e("FastGPT_API", "错误响应: $errorBody")
                    throw Exception("API请求失败: ${response.code}")
                }

                val reader = BufferedReader(
                    InputStreamReader(
                        response.body?.byteStream() ?: throw Exception("响应为空")
                    )
                )

                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    Log.d("FastGPT_API", "Stream line: $line")
                    if (line?.startsWith("data: ") == true) {
                        val jsonData = line?.substring(6)
                        if (jsonData != null && jsonData.isNotEmpty() && !jsonData.contains("\"content\":\"\"")) {
                            try {
                                val streamResponse =
                                    gson.fromJson(jsonData, StreamResponse::class.java)
                                val content =
                                    streamResponse.choices.firstOrNull()?.delta?.content ?: ""
                                if (content.isNotEmpty()) {
                                    emit(content)
                                }
                            } catch (e: Exception) {
                                // 记录解析错误
                                Log.e("FastGPT_API", "解析错误: ${e.message}", e)
                            }
                        }
                    }
                }

                // 如果成功完成，跳出重试循环
                break

            } catch (e: Exception) {
                lastException = e
                Log.e("FastGPT_API", "请求失败，尝试重试 ${retryCount + 1}/${maxRetries}", e)
                retryCount++

                if (retryCount >= maxRetries) {
                    Log.e("FastGPT_API", "达到最大重试次数，放弃请求", e)
                    throw e
                }

                // 指数退避重试
                kotlinx.coroutines.delay(1000L * (1 shl retryCount))
            }
        }
    }.flowOn(Dispatchers.IO)
}