package com.example.elonmars.presentaton.viewModelTest

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.elonmars.data.exception.RequestException
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.domain.interactors.IPhotosInteractor
import com.example.elonmars.domain.provider.ISchedulersProvider
import com.example.elonmars.presentation.gallery.enums.GalleryType
import com.example.elonmars.presentation.gallery.viewmodel.GalleryViewModel
import io.mockk.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/** Класс для тестирования [GalleryViewModel] */
class GalleryViewModelTest {

    @get:Rule
    var mRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var galleryViewModel: GalleryViewModel
    private var progressLiveDataObserver: Observer<Boolean> = mockk()
    private var refreshLiveDataObserver: Observer<Boolean> = mockk()
    private var contentTypeLiveDataObserver: Observer<Int> = mockk()
    private var errorLiveDataObserver: Observer<Throwable> = mockk()
    private var favPhotoItemsLiveDataObserver: Observer<List<PhotoItem>> = mockk()
    private var photoItemsLiveDataObserver: Observer<List<PhotoItem>> = mockk()

    private val schedulersProvider: ISchedulersProvider = mockk()
    private val photosInteractor: IPhotosInteractor = mockk()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        galleryViewModel = GalleryViewModel(photosInteractor, schedulersProvider)
        galleryViewModel.getProgressLiveData().observeForever(progressLiveDataObserver)
        galleryViewModel.getErrorLiveData().observeForever(errorLiveDataObserver)
        galleryViewModel.getPhotoItemsLiveData().observeForever(photoItemsLiveDataObserver)
        galleryViewModel.getRefreshingProgressLiveData().observeForever(refreshLiveDataObserver)
        galleryViewModel.getFavPhotoItemsLiveData().observeForever(favPhotoItemsLiveDataObserver)
        galleryViewModel.getContentTypeLiveData().observeForever(contentTypeLiveDataObserver)

        every { Log.e(any(), any()) } returns 0
        every { schedulersProvider.io() } returns Schedulers.trampoline()
        every { schedulersProvider.ui() } returns Schedulers.trampoline()

        every { progressLiveDataObserver.onChanged(any()) } just Runs
        every { errorLiveDataObserver.onChanged(any()) } just Runs
        every { photoItemsLiveDataObserver.onChanged(any()) } just Runs
        every { refreshLiveDataObserver.onChanged(any()) } just Runs
        every { favPhotoItemsLiveDataObserver.onChanged(any()) } just Runs
        every { contentTypeLiveDataObserver.onChanged(any()) } just Runs
    }

    @Test
    fun loadDataAsyncTest() {
        // Arrange
        every { photosInteractor.loadPhotosAsync() } returns Single.just(createData())

        // Act
        galleryViewModel.loadDataAsync()

        // Assert
        verify(exactly = 1) { photosInteractor.loadPhotosAsync() }
        verify { errorLiveDataObserver wasNot called }
        verifyOrder {
            progressLiveDataObserver.onChanged(true)
            photoItemsLiveDataObserver.onChanged(any())
            progressLiveDataObserver.onChanged(false)
        }
    }

    @Test
    fun loadDataAsyncTestFail() {
        // Arrange
        val exception = RequestException("It's a test")
        every { photosInteractor.loadPhotosAsync() } returns Single.error(exception)

        // Act
        galleryViewModel.loadDataAsync()

        // Assert
        verify(exactly = 0) { photoItemsLiveDataObserver.onChanged(any()) }
        verify(exactly = 1) { errorLiveDataObserver.onChanged(any()) }

        verifyOrder {
            progressLiveDataObserver.onChanged(true)
            errorLiveDataObserver.onChanged(exception)
            progressLiveDataObserver.onChanged(false)
        }
    }

    @Test
    fun loadDataOnForceAsync() {
        // Arrange
        every { photosInteractor.loadPhotosOnCall() } returns Single.just(createData())

        // Act
        galleryViewModel.loadDataOnForceAsync()

        // Assert
        verify(exactly = 1) { photosInteractor.loadPhotosOnCall() }
        verify { errorLiveDataObserver wasNot called }
        verifyOrder {
            refreshLiveDataObserver.onChanged(true)
            photoItemsLiveDataObserver.onChanged(any())
            refreshLiveDataObserver.onChanged(false)
        }
    }

    @Test
    fun loadDataOnForceAsyncFail() {
        // Arrange
        val exception = RequestException("It's a test")
        every { photosInteractor.loadPhotosOnCall() } returns Single.error(exception)

        // Act
        galleryViewModel.loadDataOnForceAsync()

        // Assert
        verify(exactly = 0) { photoItemsLiveDataObserver.onChanged(any()) }
        verify(exactly = 1) { errorLiveDataObserver.onChanged(any()) }

        verifyOrder {
            refreshLiveDataObserver.onChanged(true)
            errorLiveDataObserver.onChanged(exception)
            refreshLiveDataObserver.onChanged(false)
        }
    }

    @Test
    fun getFavouritePhotosTest() {
        // Arrange
        every { photosInteractor.getFavouritePhotos() } returns getFavouritePhotos()

        // Act
        galleryViewModel.getFavouritePhotos()

        // Assert
        verify(exactly = 1) { photosInteractor.getFavouritePhotos() }
        verify(exactly = 1) { favPhotoItemsLiveDataObserver.onChanged(any()) }
    }

    @Test
    fun getFavouritePhotosEmptyListTest() {
        // Arrange
        every { photosInteractor.getFavouritePhotos() } returns listOf()

        // Act
        galleryViewModel.getFavouritePhotos()

        // Assert
        verify(exactly = 1) { photosInteractor.getFavouritePhotos() }
        verify(exactly = 1) { favPhotoItemsLiveDataObserver.onChanged(any()) }
    }

    @Test
    fun setFavouriteGalleryTypeRandomTest() {
        // Arrange
        val favouritePhoto = PhotoItem("title")
        every { photosInteractor.setFavourite(favouritePhoto) } just Runs
        every { photosInteractor.getGalleryType() } returns GalleryType.RANDOM.ordinal

        // Act
        galleryViewModel.setFavourite(favouritePhoto)

        // Assert
        verify(exactly = 1) { photosInteractor.setFavourite(favouritePhoto) }
        verify(exactly = 0) { favPhotoItemsLiveDataObserver.onChanged(any()) }
    }

    @Test
    fun setFavouriteGalleryTypeFavouriteTest() {
        // Arrange
        val favouritePhoto = PhotoItem("title")
        every { photosInteractor.setFavourite(favouritePhoto) } just Runs
        every { photosInteractor.getGalleryType() } returns GalleryType.FAVOURITE.ordinal
        every { photosInteractor.getFavouritePhotos() } returns getFavouritePhotos()

        // Act
        galleryViewModel.setFavourite(favouritePhoto)

        // Assert
        verify(exactly = 1) { photosInteractor.setFavourite(favouritePhoto) }
        verify(exactly = 1) { favPhotoItemsLiveDataObserver.onChanged(any()) }
    }

    @Test
    fun setContentTypeTest() {
        // Arrange
        every { photosInteractor.setGalleryType(GalleryType.FAVOURITE.ordinal) } just Runs

        // Act
        galleryViewModel.setContentType(GalleryType.FAVOURITE)

        // Assert
        verify(exactly = 1) { photosInteractor.setGalleryType(GalleryType.FAVOURITE.ordinal) }
        verify(exactly = 1) { contentTypeLiveDataObserver.onChanged(any()) }
    }

    private fun createData(): List<PhotoItem> {
        val list = arrayListOf<PhotoItem>()
        list.add(PhotoItem("title1", "date1", "image23", "explanation45"))
        list.add(PhotoItem("title2", "date2", "45", "explanation67"))
        list.add(PhotoItem("title3", "date3", "3", "explanation5"))

        return list
    }

    private fun getFavouritePhotos(): List<PhotoItem> {
        val list = arrayListOf<PhotoItem>()
        list.add(PhotoItem("title1", "date1", isFavourite = true))
        list.add(PhotoItem("title2", "date2", isFavourite = true))
        list.add(PhotoItem("title3", "date3", isFavourite = true))

        return list
    }
}
