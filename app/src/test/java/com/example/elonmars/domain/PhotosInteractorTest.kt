package com.example.elonmars.domain

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.domain.interactors.PhotosInteractor
import com.example.elonmars.domain.repositories.IPhotosRepository
import io.mockk.*
import io.reactivex.Single
import org.junit.Test

/** Класс для тестирования [PhotosInteractor] */
class PhotosInteractorTest {

    private val photosRepository: IPhotosRepository = mockk()
    private val photosInteractor = PhotosInteractor(photosRepository)

    @Test
    fun loadPhotosAsyncTest() {
        every { photosRepository.getFavouritePhotosFromCache() } returns getFavList()
        every { photosRepository.loadPhotosAsync() } returns Single.just(getList())

        val testObserver = photosInteractor.loadPhotosAsync().test()

        verify(exactly = 1) { photosRepository.getFavouritePhotosFromCache() }
        verify(exactly = 1) { photosRepository.loadPhotosAsync() }
        testObserver.assertResult(getList())
        testObserver.assertNever(listOf())
        testObserver.dispose()
    }

    @Test
    fun loadPhotosAsyncVideoMediaTypeTest() {
        every { photosRepository.getFavouritePhotosFromCache() } returns getFavList()
        every { photosRepository.loadPhotosAsync() } returns Single.just(getListWithVideo())

        val testObserver = photosInteractor.loadPhotosAsync().test()

        verify(exactly = 1) { photosRepository.getFavouritePhotosFromCache() }
        verify(exactly = 1) { photosRepository.loadPhotosAsync() }
        testObserver.assertResult(
            listOf(
                PhotoItem("title1", media_type = "image"),
                PhotoItem("title3", media_type = "image")
            )
        )
        testObserver.assertNever(getList())
        testObserver.dispose()
    }

    @Test
    fun loadPhotosOnCallTest() {
        every { photosRepository.loadPhotosOnCall() } returns Single.just(getList())

        val testObserver = photosInteractor.loadPhotosOnCall().test()

        verify(exactly = 1) { photosRepository.loadPhotosOnCall() }
        testObserver.assertResult(getList())
        testObserver.assertNever(listOf())
        testObserver.dispose()
    }

    @Test
    fun loadPhotosOnCallVideoMediaTypeTest() {
        every { photosRepository.loadPhotosOnCall() } returns Single.just(getListWithVideo())

        val testObserver = photosInteractor.loadPhotosOnCall().test()

        verify(exactly = 1) { photosRepository.loadPhotosOnCall() }
        testObserver.assertResult(
            listOf(
                PhotoItem("title1", media_type = "image"),
                PhotoItem("title3", media_type = "image")
            )
        )
        testObserver.assertNever(listOf())
        testObserver.assertNever(getList())
        testObserver.dispose()
    }

    @Test
    fun setFavouriteTest() {
        val photoItem = PhotoItem("title1", isFavourite = false)
        every { photosRepository.setFavourite(photoItem) } just Runs

        photosRepository.setFavourite(photoItem)

        verify(exactly = 1) { photosRepository.setFavourite(photoItem) }
    }


    private fun getFavList(): List<PhotoItem> =
        listOf(
            PhotoItem("title1", media_type = "image"),
            PhotoItem("title2", media_type = "image")
        )

    private fun getList(): List<PhotoItem> = listOf(
        PhotoItem("title1", media_type = "image"),
        PhotoItem("title2", media_type = "image"),
        PhotoItem("title3", media_type = "image")
    )

    private fun getListWithVideo(): List<PhotoItem> = listOf(
        PhotoItem("title1", media_type = "image"),
        PhotoItem("title2", media_type = "video"),
        PhotoItem("title3", media_type = "image")
    )
}
