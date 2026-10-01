package com.z9tether.model

data class DeviceInfo(
    val manufacturer: String = "",
    val model: String = "",
    val version: String = "",
    val serialNumber: String = ""
)

data class PtpStorageId(val value: UInt)

data class PtpObjectInfo(
    val storageId: UInt,
    val objectFormat: UShort,
    val objectCompressedSize: UInt,
    val filename: String
)
