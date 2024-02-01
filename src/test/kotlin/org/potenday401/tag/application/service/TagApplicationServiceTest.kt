import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.potenday401.tag.application.service.TagApplicationService
import org.potenday401.tag.domain.model.Tag
import org.potenday401.tag.domain.model.TagRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(MockitoJUnitRunner::class)
class TagApplicationServiceTest {

    @Mock
    private lateinit var tagRepository: TagRepository

    private lateinit var tagApplicationService: TagApplicationService

    @Before
    fun setUp() {
        tagApplicationService = TagApplicationService(tagRepository)
    }

    @Test
    fun testGetTagById() {
        val tagId = "tag-id-1"
        val tagMemberId = "tag-member-id-1"
        val tagName = "tag-name-1"
        val mockTag = Tag(tagId, tagMemberId, tagName)
        `when`(tagRepository.findById(tagId)).thenReturn(mockTag)

        val tagData = tagApplicationService.getTagById(tagId)

        assertNotNull(tagData)
        assertEquals(tagId, tagData.tagId)
        assertEquals(tagName, tagData.name)
    }

    @Test
    fun testGetTagById_nonExistingId_returnsNull() {
        val tagId = "tag-id-1"
        `when`(tagRepository.findById(tagId)).thenReturn(null)

        val tagData = tagApplicationService.getTagById(tagId)

        assertNull(tagData)
    }

}
