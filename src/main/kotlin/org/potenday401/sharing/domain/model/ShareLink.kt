package org.potenday401.sharing.domain.model

import java.time.LocalDateTime

class ShareLink(val id: Int?, val memberId: String, val ogKey: String, val ogUrl: String, val deepLink: String, val createdAt: LocalDateTime = LocalDateTime.now()) {
    constructor(memberId: String, ogKey: String, ogUrl: String, deepLink: String, createdAt: LocalDateTime = LocalDateTime.now())
            : this(null, memberId, ogKey, ogUrl, deepLink, createdAt)
}