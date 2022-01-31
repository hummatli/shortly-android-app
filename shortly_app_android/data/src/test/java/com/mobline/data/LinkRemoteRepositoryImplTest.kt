package com.mobline.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mobline.data.remote.link.ShrtCodeApi
import com.mobline.data.repository.LinkRemoteRepositoryImpl
import com.mobline.domain.model.link.Link
import com.mobline.domain.repository.LinkRemoteRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.io.IOException
import java.util.concurrent.TimeUnit

@ExperimentalSerializationApi
class LinkRemoteRepositoryImplTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: OkHttpClient
    private lateinit var api: ShrtCodeApi
    private lateinit var repo: LinkRemoteRepository


    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .build()

        api = Retrofit.Builder()
            .client(client)
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                Json {
                    isLenient = true
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(ShrtCodeApi::class.java)

        repo = LinkRemoteRepositoryImpl(api)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    @Throws(Exception::class)
    fun shortenLinkIn200Response() {
        mockWebServer.enqueueResponse("shorten-link-200.json", 200)

        runBlocking {
            val actualLink = repo.shortenLink("any link")

            val expected = Link(
                    code = "KCveN",
                    shortLink = "https://shrtco.de/KCveN",
                    longLink = "http://example.org/very/long/link.html",
                )

            assertEquals(expected, actualLink)
        }
    }
}