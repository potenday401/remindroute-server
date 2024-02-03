package org.potenday401.sharing.application.dto

import kotlinx.serialization.Serializable

@Serializable
class ShareLinkCreationData(val ogImageBase64Payload:String, val fileExt:String, val deepLink:String, val memberId:String)