package org.potenday401.tag.domain.model


interface TagRepository {
    fun findById(id: String): Tag?
    fun findAllByNameIn(memberId: String, names: List<String>): List<Tag>
    fun findAllByMemberId(memberId: String): List<Tag>
    fun findAll(memberId: String): List<Tag>
    fun create(tag: Tag)
}
