package com.example.trackerapp.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = RegisterResponseSerializer::class)
data class RegisterResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("message")
    val message: String,

    @SerialName("category")
    val category: Category? = null
)

@Serializable
data class Category(
    @SerialName("_id")
    val id: String
)

@Serializable
@SerialName("RegisterResponse")
private data class RegisterResponseSurrogate(
    val success: Boolean,
    val message: String,
    val id: String?
)

object RegisterResponseSerializer : KSerializer<RegisterResponse> {
    override val descriptor: SerialDescriptor
        get() = RegisterResponseSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): RegisterResponse {
        val surrogate = decoder.decodeSerializableValue(RegisterResponseSurrogate.serializer())
        return RegisterResponse(surrogate.success, surrogate.message,
            surrogate.id?.let { Category(it) })
    }

    override fun serialize(encoder: Encoder, value: RegisterResponse) {
        val surrogate = RegisterResponseSurrogate(value.success, value.message, value.category?.id)
        encoder.encodeSerializableValue(RegisterResponseSurrogate.serializer(), surrogate)
    }
}