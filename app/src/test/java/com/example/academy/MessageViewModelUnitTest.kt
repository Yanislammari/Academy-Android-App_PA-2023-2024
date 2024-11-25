package com.example.academy

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.example.academy.viewmodels.MessageViewModel
import com.example.academy.network.repository.MessageRepository
import org.mockito.Mockito
import java.text.SimpleDateFormat
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28]) // Utilisez la version de SDK appropriée pour votre projet
class MessageViewModelUnitTest {
    private val messageRepository = Mockito.mock(MessageRepository::class.java)
    private val viewModel = MessageViewModel(messageRepository)

    @Test
    fun testParseDate_validDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val dateString = "2024-07-26T14:00:00.000Z"
        val expectedDate = dateFormat.parse(dateString)

        val parsedDate = viewModel.parseDate(dateString, dateFormat)

        assertEquals(expectedDate, parsedDate)
    }

    @Test
    fun testParseDate_invalidDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val dateString = "invalid-date-string"

        val parsedDate = viewModel.parseDate(dateString, dateFormat)

        assertNull(parsedDate)
    }

    @Test
    fun testParseDate_emptyDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val dateString = ""

        val parsedDate = viewModel.parseDate(dateString, dateFormat)

        assertNull(parsedDate)
    }

    @Test
    fun testParseDate_nullDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

        val parsedDate = viewModel.parseDate(null.toString(), dateFormat)

        assertNull(parsedDate)
    }

    @Test
    fun testParseDate_differentFormat() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val dateString = "26/07/2024 14:00:00"
        val expectedDate = dateFormat.parse(dateString)

        val parsedDate = viewModel.parseDate(dateString, dateFormat)

        assertEquals(expectedDate, parsedDate)
    }
}
