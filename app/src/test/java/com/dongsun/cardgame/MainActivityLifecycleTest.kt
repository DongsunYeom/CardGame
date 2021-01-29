package com.dongsun.cardgame

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dongsun.cardgame.models.Card
import com.dongsun.cardgame.viewmodels.MainActivityViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class MainActivityLifecycleTest {
    @get:Rule val testRule = InstantTaskExecutorRule()
    private val viewModel = MainActivityViewModel()

    @Test
    fun whenUpdatingLifecycle_thenLiveDataUpdates() {
        viewModel.cardList.postValue(viewModel.createCardGame())

        // Arrange
        val liveDataTestUtil: LiveDataTestUtil<ArrayList<Card>> = LiveDataTestUtil()
        // Act
        val note = liveDataTestUtil.getValue(viewModel.cardLiveData)
        // Assert
        if (note != null) {
            assertEquals(note.size, 12)
        }
    }

}

