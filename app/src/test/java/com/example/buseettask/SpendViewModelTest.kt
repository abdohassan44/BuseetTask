package com.example.buseettask

import android.content.Context
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SpendViewModelTest : TestCase() {

    private lateinit var spendsDatabase: SpendsDatabase
    private lateinit var viewModel: SpendViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        spendsDatabase = Room.inMemoryDatabaseBuilder(
            context, SpendsDatabase::class.java
        ).allowMainThreadQueries().build()
        val dataSource = SpendsTrackerDataSource(spendsDatabase.getSpendDao())
        viewModel = SpendViewModel(dataSource)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        spendsDatabase.close()
    }

    @Test
    fun testAddingSpend() {
        viewModel.addSpend(100, "Eggs")
        viewModel.getLast20Spends()
        val result = viewModel.last20SpendsLiveData.getOrAwaitValue().find {
            it.amount == 100 && it.description == "Eggs"
        }
        assertThat(result != null).isTrue()
    }
}