package com.example.trackerapp.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

//@Serializable(with = OtpResponseSerializer::class)
@Serializable
data class OtpResponse(
    @SerialName("message")
    val message: String,

    @SerialName("existingUser")
    var existingUser: User? = null,

    @SerialName("newUser")
    var newUser: User? = null
)

@Serializable
data class User(
    @SerialName("_id")
    val id: String = ""
)

//@Serializable
//@SerialName("OtpResponse")
//private data class OtpResponseSurrogate(
//    val message: String,
//    val id: String
//)
//
//object OtpResponseSerializer : KSerializer<OtpResponse> {
//    override val descriptor: SerialDescriptor
//        get() = OtpResponseSurrogate.serializer().descriptor
//
//    override fun deserialize(decoder: Decoder): OtpResponse {
//        val surrogate = decoder.decodeSerializableValue(OtpResponseSurrogate.serializer())
//        return OtpResponse(surrogate.message, ExistingUser(surrogate.id))
//    }
//
//    override fun serialize(encoder: Encoder, value: OtpResponse) {
//        val surrogate = OtpResponseSurrogate(value.message, value.existingUser.id)
//        encoder.encodeSerializableValue(OtpResponseSurrogate.serializer(), surrogate)
//    }
//}