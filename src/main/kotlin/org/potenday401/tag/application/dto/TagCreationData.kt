package org.potenday401.tag.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class TagCreationData(val tagId:String, val memberId:String, val name:String) {

}