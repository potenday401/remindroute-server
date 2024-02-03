package org.potenday401.sharing.domain.model

interface ShareLinkRepository {

    fun create(shareLink: ShareLink)
    fun findByOgKey(ogKey: String): ShareLink?
}