import PhotoPinTable.photoUrl
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.potenday401.common.domain.model.FileStorageService
import org.potenday401.photopin.application.dto.LatLngData
import org.potenday401.photopin.application.dto.PhotoPinMutationData
import org.potenday401.photopin.application.service.PhotoPinApplicationService
import org.potenday401.photopin.domain.model.LatLng
import org.potenday401.photopin.domain.model.PhotoPin
import org.potenday401.photopin.domain.model.PhotoPinRepository
import org.potenday401.photopin.domain.model.mockPhotoPin1
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(MockitoJUnitRunner::class)
class PhotoPinApplicationServiceTest {

    @Mock
    private lateinit var photoPinRepository: PhotoPinRepository

    @Mock
    private lateinit var fileStorageService: FileStorageService

    private lateinit var photoPinApplicationService: PhotoPinApplicationService

    @Before
    fun setUp() {
        photoPinApplicationService = PhotoPinApplicationService(photoPinRepository, fileStorageService)
    }

    @Test
    fun testGetPhotoPinById() {
        `when`(photoPinRepository.findById(mockPhotoPin1.id)).thenReturn(mockPhotoPin1)

        val photoPinData = photoPinApplicationService.getPhotoPinById(mockPhotoPin1.id)

        assertNotNull(photoPinData)
        assertEquals(mockPhotoPin1.id, photoPinData.photoPinId)

    }

    @Test
    fun testGetPhotoPinById_nonExistingId_returnsNull() {
        val photoPinId = "photoPin-id-1"
        `when`(photoPinRepository.findById(photoPinId)).thenReturn(null)

        val photoPinData = photoPinApplicationService.getPhotoPinById(photoPinId)

        assertNull(photoPinData)
    }

    @Test
    fun testUpdatePhotoPin() {
        `when`(photoPinRepository.findById(mockPhotoPin1.id)).thenReturn(mockPhotoPin1)

        val tagIds = listOf("tagId1","tagId2")
        val photoPinMutationData = PhotoPinMutationData(mockPhotoPin1.id, tagIds, null, null,null, LatLngData(1.0,2.0))
        photoPinApplicationService.updatePhotoPin(photoPinMutationData)

        mockPhotoPin1.tagIds = photoPinMutationData.tagIds
        mockPhotoPin1.latLng = LatLng(photoPinMutationData.latLng.latitude, photoPinMutationData.latLng.longitude)

        verify(photoPinRepository).update(mockPhotoPin1)
    }

}
