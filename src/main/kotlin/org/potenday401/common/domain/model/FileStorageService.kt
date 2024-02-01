package org.potenday401.common.domain.model

import java.io.IOException
import java.net.URL

interface FileStorageService {

    @Throws(IOException::class)
    fun storeFile(directory: String, file: File): URL

    fun deleteFileByPath(path: String?)

}