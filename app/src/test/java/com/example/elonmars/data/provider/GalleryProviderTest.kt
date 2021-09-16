package com.example.elonmars.data.provider

import android.util.Log
import com.example.elonmars.BuildConfig.API_KEY
import com.example.elonmars.data.PhotoApiInterface
import com.example.elonmars.data.exception.RequestException
import com.example.elonmars.data.model.PhotoItem
import io.mockk.*
import org.junit.Assert
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

/** Класс для тестирования [GalleryProvider] */
class GalleryProviderTest {

    private val photo1 = PhotoItem("title1", isFavourite = true)
    private val photo2 = PhotoItem("title2", "19280", isFavourite = true)
    private val photo3 = PhotoItem("title3", "date", explanation = "explanation", isFavourite = true)

    private val mockRetrofitClient: Retrofit = mockk()
    private val galleryProvider = GalleryProvider(mockRetrofitClient)
    private val photoApi: PhotoApiInterface = mockk()
    private val mockCall: Call<List<PhotoItem>> = mockk()

    @Test
    fun loadPhotoItemsListTest() {
        // Arrange
        mockkStatic(Log::class)
        val data = listOf(photo1, photo2, photo3)

        every { Log.e(any(), any()) } returns 0
        every { mockRetrofitClient.create(PhotoApiInterface::class.java) } returns photoApi
        every { photoApi.getPhotos(API_KEY, 10) } returns mockCall
        every { mockCall.execute() } returns Response.success(data)

        // Act
        val result = galleryProvider.loadPhotoItemsList()

        // Assert
        Assert.assertEquals(data, result)
        Assert.assertEquals(data[0], result[0])
        Assert.assertEquals(data[1], result[1])
        Assert.assertEquals(data[2], result[2])
        verify(exactly = 1) { mockCall.execute() }
    }

    @Test
    fun loadPhotoItemsListFailTest() {
        // Arrange
        mockkStatic(Log::class)
        val response = mockk<Response<List<PhotoItem>>>()

        every { Log.e(any(), any()) } returns 0
        every { mockRetrofitClient.create(PhotoApiInterface::class.java) } returns photoApi
        every { photoApi.getPhotos(API_KEY, 10) } returns mockCall
        every { response.code() } returns 404
        every { response.isSuccessful } returns false
        every { mockCall.execute() } returns response

        // Act
        val result = galleryProvider.loadPhotoItemsList()

        // Assert
        verify { Log.e(any(), any()) }
        Assert.assertEquals(listOf<PhotoItem>(), result)
    }

    @Test
    fun loadPhotoItemsListFailBodyNullTest() {
        // Arrange
        mockkStatic(Log::class)
        val response = mockk<Response<List<PhotoItem>>>()

        every { Log.e(any(), any()) } returns 0
        every { mockRetrofitClient.create(PhotoApiInterface::class.java) } returns photoApi
        every { photoApi.getPhotos(API_KEY, 10) } returns mockCall
        every { response.code() } returns 404

        every { mockCall.execute() } throws RequestException("Body is null")

        try {
            // Act
            galleryProvider.loadPhotoItemsList()
        } catch (e: Exception) {
            // Assert
            Assert.assertEquals("Body is null", e.message)
            verify(exactly = 0) { Log.e(any(), any()) }
        }
    }

    @Test
    fun loadPhotoItemsListFailBodyEmptyTest() {
        // Arrange
        mockkStatic(Log::class)

        every { Log.e(any(), any()) } returns 0
        every { mockRetrofitClient.create(PhotoApiInterface::class.java) } returns photoApi
        every { photoApi.getPhotos(API_KEY, 10) } returns mockCall

        every { mockCall.execute() } returns Response.success(listOf())

        // Act
        val result = galleryProvider.loadPhotoItemsList()

        // Assert
        Assert.assertEquals(listOf<PhotoItem>(), result)
    }
}
