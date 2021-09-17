package com.example.elonmars.presentaton.viewModelTest

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.domain.interactors.IPhotosInteractor
import com.example.elonmars.domain.provider.ISchedulersProvider
import com.example.elonmars.presentation.detail_photo.viewmodel.DetailPhotoViewModel
import io.mockk.*
import org.junit.Test

/** Класс для тестирования [DetailPhotoViewModel] */
class DetailPhotoViewModelTest {

    private val photosInteractor: IPhotosInteractor = mockk()
    private val schedulersProvider: ISchedulersProvider = mockk()
    private val detailPhotoViewModel = DetailPhotoViewModel(photosInteractor, schedulersProvider)

    @Test
    fun setFavourite() {
        // Arrange
        val favouritePhoto = PhotoItem("title")
        every { photosInteractor.setFavourite(favouritePhoto) } just Runs

        // Act
        detailPhotoViewModel.setFavourite(favouritePhoto)

        // Assert
        verify(exactly = 1) { photosInteractor.setFavourite(favouritePhoto) }
    }
}