package org.potenday401.tag.domain.model


interface TagRepository {
    fun findById(id: String): Tag?
    fun findAllByNameIn(names: List<String>): List<Tag>
    fun findAllByMemberId(memberId: String): List<Tag>
    fun findAll(): List<Tag>
    fun create(tag: Tag)
}
