package org.potenday401.common.domain.model

import java.lang.IllegalArgumentException

class File(val payload: ByteArray, val ext: String) {
    private val fileMaxSize = 52428800 // 50MB

    init {
        validateFileSize()
    }

    private fun validateFileSize() {
        if (payload.size < fileMaxSize) {
            return
        }
        throw IllegalArgumentException("max file size:${fileMaxSize} Bytes")
    }

}
