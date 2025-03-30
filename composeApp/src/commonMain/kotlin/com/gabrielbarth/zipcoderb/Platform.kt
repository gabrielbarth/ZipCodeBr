package com.gabrielbarth.zipcoderb

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform