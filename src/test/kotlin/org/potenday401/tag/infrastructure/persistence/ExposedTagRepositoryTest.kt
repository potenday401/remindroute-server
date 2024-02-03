package org.potenday401.tag.infrastructure.persistence

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.potenday401.tag.domain.model.Tag
import java.time.LocalDateTime
import java.util.*

class ExposedTagRepositoryTest {

    val repository = ExposedTagRepository()

    @Before
    fun setup() {
        // 각각의 테스트마다 새로운 DB 사용
        val uniqueId = UUID.randomUUID().toString()
        Database.connect("jdbc:h2:mem:test-$uniqueId;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(TagTable)

            TagTable.insert {
                it[id] = "test-id-1"
                it[memberId] = "test-member-id-1"
                it[name] = "test-name-1"
                it[createdAt] = LocalDateTime.now()
                it[modifiedAt] = LocalDateTime.now()
            }

            TagTable.insert {
                it[id] = "test-id-2"
                it[memberId] = "test-member-id-1"
                it[name] = "test-name-2"
                it[createdAt] = LocalDateTime.now()
                it[modifiedAt] = LocalDateTime.now()
            }
        }
    }

    @Test
    fun testFindById() {
        val foundTag = repository.findById("test-id-1")

        assertNotNull(foundTag)
        assertEquals("test-id-1", foundTag?.id)
        assertEquals("test-member-id-1", foundTag?.memberId)
        assertEquals("test-name-1", foundTag?.name)
    }

    @Test
    fun testCreate() {
        val currentTime = LocalDateTime.now()
        val tag = Tag("test-id-3", "test-member-id-3","test-name-3", currentTime, currentTime)

        transaction {
            repository.create(tag)
            val result = TagTable.select { TagTable.id eq "test-id-3" }.single()
            assertEquals("test-id-3", result[TagTable.id])
            assertEquals("test-member-id-3", result[TagTable.memberId])
            assertEquals("test-name-3", result[TagTable.name])
        }
    }

    @Test
    fun testFindAll() {
        val tags = repository.findAll("test-member-id-1")

        assertTrue(tags.stream().anyMatch { tag ->
            tag.id == "test-id-1" && tag.name == "test-name-1"
        })

        assertTrue(tags.stream().anyMatch { tag ->
            tag.id == "test-id-2" && tag.name == "test-name-2"
        })
    }

    @Test
    fun testFindAllByNameIn() {
        val tags = repository.findAllByNameIn("test-member-id-1",listOf("test-name-1", "test-name-2"))

        assertEquals(2, tags.size)
    }

}