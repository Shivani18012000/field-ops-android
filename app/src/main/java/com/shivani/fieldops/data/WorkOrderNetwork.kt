package com.shivani.fieldops.data

import com.shivani.fieldops.BuildConfig
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class WorkOrderDto(
    val id: String,
    val title: String,
    val site: String,
    val priority: String,
    val status: String,
    val updatedAt: String,
)

interface WorkOrderApi {
    @GET("work-orders")
    suspend fun getWorkOrders(): List<WorkOrderDto>
}

class DemoWorkOrderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val json = """[
          {"id":"WO-301","title":"Replace router","site":"La Jolla Clinic","priority":"HIGH","status":"OPEN","updatedAt":"2026-10-08T13:30:00Z"},
          {"id":"WO-300","title":"Inspect payment terminal","site":"Mission Valley Store","priority":"MEDIUM","status":"IN_PROGRESS","updatedAt":"2026-10-08T12:45:00Z"},
          {"id":"WO-299","title":"Verify inventory scanner","site":"Chula Vista Warehouse","priority":"LOW","status":"COMPLETE","updatedAt":"2026-10-08T09:10:00Z"}
        ]""".trimIndent()
        return Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("Demo response")
            .body(json.toResponseBody("application/json".toMediaType()))
            .build()
    }
}

fun createWorkOrderApi(): WorkOrderApi {
    val client = OkHttpClient.Builder()
        .apply { if (BuildConfig.USE_DEMO_BACKEND) addInterceptor(DemoWorkOrderInterceptor()) }
        .build()
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WorkOrderApi::class.java)
}
