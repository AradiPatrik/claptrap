package com.aradipatrik.claptrap.backend

import com.squareup.moshi.Moshi
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.util.pipeline.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.Okio
import kotlin.reflect.jvm.jvmErasure

/**
 * Moshi converter for [ContentNegotiation] feature
 */
class MoshiConverter(private val moshi: Moshi = Moshi.Builder().build()) : ContentConverter {
  override suspend fun convertForSend(
    context: PipelineContext<Any, ApplicationCall>,
    contentType: ContentType,
    value: Any
  ): Any? = TextContent(
    moshi.adapter(value.javaClass).toJson(value),
    contentType.withCharset(context.call.suitableCharset())
  )

  override suspend fun convertForReceive(context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>): Any? {
    val request = context.subject
    val channel = request.value as? ByteReadChannel ?: return null
    val type = request.typeInfo
    val javaType = type.jvmErasure

    return withContext(Dispatchers.IO) {
      val buffer = Okio.buffer(Okio.source(channel.toInputStream()))
      moshi.adapter(javaType.javaObjectType).fromJson(buffer)
    }
  }
}

/**
 * Register Moshi to [ContentNegotiation] feature
 */
fun ContentNegotiation.Configuration.moshi(
  contentType: ContentType = ContentType.Application.Json,
  block: Moshi.Builder.() -> Unit = {}
) {
  val builder = Moshi.Builder()
  builder.apply(block)
  val converter = MoshiConverter(builder.build())
  register(contentType, converter)
}
