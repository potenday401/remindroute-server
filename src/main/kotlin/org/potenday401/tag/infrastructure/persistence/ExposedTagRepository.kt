package org.potenday401.tag.infrastructure.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import org.potenday401.tag.domain.model.Tag
import org.potenday401.tag.domain.model.TagRepository


object TagTable : Table("tag") {
    val id: Column<String> = varchar("id", 64)
    val memberId: Column<String> = varchar("member_id", 64)
    val name: Column<String> = varchar("name", 128)
    val createdAt = datetime("created_at")
    val modifiedAt = datetime("modified_at")

    override val primaryKey = PrimaryKey(id)
}

class ExposedTagRepository : TagRepository {
    override fun findById(id: String): Tag? {
        return transaction {
            val result = TagTable.select { TagTable.id eq id }.map {
                Tag(
                    id = it[TagTable.id],
                    name = it[TagTable.name],
                    memberId = it[TagTable.memberId],
                    createdAt = it[TagTable.createdAt],
                    modifiedAt = it[TagTable.modifiedAt]
                )
            }.firstOrNull()
            return@transaction result
        }
    }

    override fun findAllByNameIn(names: List<String>): List<Tag> {
        return transaction {
            TagTable.select { TagTable.name inList names }.map { row ->
                Tag(
                    id = row[TagTable.id],
                    name = row[TagTable.name],
                    memberId = row[TagTable.memberId],
                    createdAt = row[TagTable.createdAt],
                    modifiedAt = row[TagTable.modifiedAt]
                )
            }
        }
    }

    override fun findAllByMemberId(memberId: String): List<Tag> {
        return transaction {
            TagTable.select { TagTable.memberId eq memberId }.map { row ->
                Tag(
                    id = row[TagTable.id],
                    name = row[TagTable.name],
                    memberId = row[TagTable.memberId],
                    createdAt = row[TagTable.createdAt],
                    modifiedAt = row[TagTable.modifiedAt]
                )
            }
        }
    }

    override fun findAll(): List<Tag> {
        return transaction {
            TagTable.selectAll().map { row ->
                Tag(
                    id = row[TagTable.id],
                    name = row[TagTable.name],
                    memberId = row[TagTable.memberId],
                    createdAt = row[TagTable.createdAt],
                    modifiedAt = row[TagTable.modifiedAt]
                )
            }
        }
    }

    override fun create(tag: Tag) {
        transaction {
            TagTable.insert {
                it[id] = tag.id
                it[name] = tag.name
                it[memberId] = tag.memberId
                it[createdAt] = tag.createdAt
                it[modifiedAt] = tag.modifiedAt
            }
        }
    }

}