package org.potenday401.sharing.application.service

import com.typesafe.config.ConfigFactory
import org.potenday401.common.domain.model.File
import org.potenday401.common.domain.model.FileStorageService
import org.potenday401.sharing.application.dto.ShareLinkCreationData
import org.potenday401.sharing.domain.model.ShareLink
import org.potenday401.sharing.domain.model.ShareLinkRepository
import java.util.*

class ShareLinkApplicationService(
    private val fileStorageService: FileStorageService,
    private val shareLinkRepository: ShareLinkRepository) {

    companion object {
        private val config = ConfigFactory.load()
        val domain: String = config.getString("ktor.aws.ec2.domain")
    }


    fun createOgImage(shareLinkCreationData: ShareLinkCreationData): String {
        val file = createFile(
            shareLinkCreationData.ogImageBase64Payload,
            shareLinkCreationData.fileExt)
        val dir = "images/${shareLinkCreationData.memberId}"
        return fileStorageService.storeFile(dir, file).toExternalForm()
    }

    private fun createFile(base64Payload: String, ext: String): File {
        val fileByte: ByteArray = Base64.getDecoder().decode(base64Payload)
        return File(fileByte, ext)
    }

    fun saveShareLinkMetadataAndGetUrl(memberId: String, imgUrl: String, deepLink: String): String {
        val ogKey = System.currentTimeMillis().toString() + memberId
        shareLinkRepository.create(ShareLink(memberId, ogKey, imgUrl, deepLink))
        return "$domain/popin/share/$ogKey"
    }

    fun findByOgKey(ogKey: String): ShareLink? {
        return shareLinkRepository.findByOgKey(ogKey)
    }

}