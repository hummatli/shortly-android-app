package com.mobline.data.di

import android.annotation.SuppressLint
import android.content.ClipboardManager
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mobline.data.errors.GeneralErrorMapper
import com.mobline.data.local.AppDatabase
import com.mobline.data.remote.link.ShrtCodeApi
import com.mobline.data.repository.LinkLocalRepositoryImpl
import com.mobline.data.repository.LinkRemoteRepositoryImpl
import com.mobline.domain.di.ERROR_MAPPER_GENERAL
import com.mobline.domain.di.IO_CONTEXT
import com.mobline.domain.exceptions.ErrorMapper
import com.mobline.domain.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.coroutines.CoroutineContext

val dataModule = module {

    single<CoroutineContext>(named(IO_CONTEXT)) { Dispatchers.IO }

    //////////////////////////////////// NETWORK ////////////////////////////////////
    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(
            if (getProperty("isDebug") == true.toString()) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )
    }

    single {
        val builder = OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .retryOnConnectionFailure(false)
            .callTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)

        if (getProperty("isDebug") == true.toString())
            builder.disableSSLVerification()

        builder.build()
    }

    single {
        Firebase.analytics
    }

    single {
        Json {
            isLenient = true
            encodeDefaults = true
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }

    single<Retrofit> {
        Retrofit.Builder()
            .client(get())
            .baseUrl(getProperty("host"))
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    factory<ShrtCodeApi> { get<Retrofit>().create(ShrtCodeApi::class.java) }


    //////////////////////////////////// REPOSITORY ////////////////////////////////////
    factory<LinkLocalRepository> {
        LinkLocalRepositoryImpl(
            db = get()
        )
    }

    factory<LinkRemoteRepository> {
        LinkRemoteRepositoryImpl(
            api = get()
        )
    }

    //////////////////////////////////// LOCAL ////////////////////////////////////
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).build()
    }

    single {
        ContextCompat.getSystemService(androidContext(), ClipboardManager::class.java)
    }


    //////////////////////////////////// ERROR MAPPER ////////////////////////////////////
    factory<ErrorMapper>(named(ERROR_MAPPER_GENERAL)) { GeneralErrorMapper(get()) }
}

fun OkHttpClient.Builder.disableSSLVerification(): OkHttpClient.Builder {
    val trustAllCerts = arrayOf<TrustManager>(
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(
                chain: Array<X509Certificate?>?,
                authType: String?,
            ) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(
                chain: Array<X509Certificate?>?,
                authType: String?,
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                return arrayOf()
            }
        }
    )

    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, SecureRandom())

    return this.sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }

}