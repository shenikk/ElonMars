package com.example.elonmars.data.repository

import com.example.elonmars.data.exception.RequestException
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.domain.provider.IGalleryProvider
import com.example.elonmars.data.store.IDataStorage
import io.mockk.*
import org.junit.Assert
import org.junit.Test

/** Класс для тестирования [PhotosRepository] */
class PhotosRepositoryTest {

    private val photo1 = PhotoItem("title1", isFavourite = true)
    private val photo2 = PhotoItem("title2", "19280", isFavourite = true)
    private val photo3 = PhotoItem("title3", "date", explanation = "explanation", isFavourite = true)

    private val dataStorage: IDataStorage = mockk()
    private val galleryProvider: IGalleryProvider = mockk()

    private val photosRepository = PhotosRepository(dataStorage, galleryProvider)

    @Test
    fun loadPhotosAsyncNoCacheTest() {
        // Arrange
        every { dataStorage.photos } returns null
        every { dataStorage.photos = getPhotos() } just Runs
        every { galleryProvider.loadPhotoItemsList() } returns getPhotos()

        // Act
        val testObserver = photosRepository.loadPhotosAsync().test()

        // Assert
        testObserver.assertResult(getPhotos())
        testObserver.assertNever(arrayListOf())
        testObserver.assertValueAt(0, getPhotos())
        verify(exactly = 1) { galleryProvider.loadPhotoItemsList() }
        verify(exactly = 1) { dataStorage.photos = getPhotos() }
        verify(exactly = 1) { dataStorage.photos }
        testObserver.dispose()
    }

    @Test
    fun loadPhotosAsyncCacheTest() {
        // Arrange
        every { dataStorage.photos } returns arrayListOf()
        every { dataStorage.photos = getPhotos() } just Runs
        every { galleryProvider.loadPhotoItemsList() } returns getPhotos()

        // Act
        val testObserver = photosRepository.loadPhotosAsync().test()

        // Assert
        testObserver.assertResult(arrayListOf())
        testObserver.assertNever(getPhotos())
        testObserver.assertValueAt(0, arrayListOf())
        verify(exactly = 0) { galleryProvider.loadPhotoItemsList() }
        verify(exactly = 0) { dataStorage.photos = getPhotos() }
        verify(exactly = 1) { dataStorage.photos }
        testObserver.dispose()
    }

    @Test
    fun loadPhotosOnCallTest() {
        // Arrange
        every { galleryProvider.loadPhotoItemsList() } returns getPhotos()
        every { dataStorage.photos = getPhotos() } just Runs

        // Act
        val testObserver = photosRepository.loadPhotosOnCall().test()

        // Assert
        testObserver.assertResult(getPhotos())
        testObserver.assertNever(arrayListOf())
        testObserver.assertValueAt(0, getPhotos())
        verify(exactly = 1) { galleryProvider.loadPhotoItemsList() }
        verify(exactly = 1) { dataStorage.photos = getPhotos() }
        testObserver.dispose()
    }

    @Test
    fun loadPhotosOnCallFailTest() {
        // Arrange
        every { galleryProvider.loadPhotoItemsList() } throws RequestException("Something went wrong")
        every { dataStorage.photos = null } just Runs

        // Act
        val testObserver = photosRepository.loadPhotosOnCall().test()

        // Assert
        testObserver.assertNever(getPhotos())
        testObserver.assertFailureAndMessage(RequestException::class.java, "Something went wrong")
        verify(exactly = 1) { galleryProvider.loadPhotoItemsList() }
        verify(exactly = 0) { dataStorage.photos = null }
        testObserver.dispose()
    }

    @Test
    fun getPhotosFromCacheTest() {
        // Arrange
        every { dataStorage.favouritePhotos } returns getPhotos()

        // Act
        val result = photosRepository.getPhotosFromCache()

        // Assert
        Assert.assertEquals(getPhotos(), result)
        Assert.assertEquals(getPhotos()[0], result[0])
        Assert.assertEquals(getPhotos()[1], result[1])
        Assert.assertEquals(getPhotos()[2], result[2])
        Assert.assertEquals(3, result.size)
    }

    @Test
    fun getPhotosFromEmptyCacheTest() {
        // Arrange
        every { dataStorage.favouritePhotos } returns null

        // Act
        val result = photosRepository.getPhotosFromCache()

        // Assert
        Assert.assertEquals(arrayListOf<PhotoItem>(), result)
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun setFavouriteRemoveTest() {
        // Arrange
        every { dataStorage.favouritePhotos } returns getPhotos()
        every { dataStorage.favouritePhotos = arrayListOf(photo1, photo2) } just Runs

        // Act
        photosRepository.setFavourite(photo3)

        // Assert
        Assert.assertEquals(arrayListOf(photo1, photo2), dataStorage.favouritePhotos)
    }

    @Test
    fun setFavouriteAddTest() {
        // Arrange
        every { dataStorage.favouritePhotos } returns arrayListOf(photo1, photo2)
        every { dataStorage.favouritePhotos = arrayListOf(photo1, photo2, photo3) } just Runs

        // Act
        photosRepository.setFavourite(photo3)

        // Assert
        Assert.assertEquals(arrayListOf(photo1, photo2, photo3), dataStorage.favouritePhotos)
    }

    @Test
    fun setFavouriteEmptyTest() {
        // Arrange
        every { dataStorage.favouritePhotos } returns arrayListOf()
        every { dataStorage.favouritePhotos = arrayListOf(photo1) } just Runs

        // Act
        photosRepository.setFavourite(photo1)

        // Assert
        Assert.assertEquals(arrayListOf(photo1), dataStorage.favouritePhotos)
    }

    private fun getPhotos(): ArrayList<PhotoItem> {
        return arrayListOf(photo1, photo2, photo3)
    }
}
