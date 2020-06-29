package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.CharacterDetails
import dev.pimentel.domain.entities.Episode
import dev.pimentel.domain.models.CharacterDetailsModel
import dev.pimentel.domain.models.EpisodeModel
import dev.pimentel.domain.repositories.CharactersRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetCharacterDetailsTest {

    private val charactersRepository = mockk<CharactersRepository>()
    private lateinit var getCharacterDetails: GetCharacterDetails

    @Test
    @BeforeEach
    fun `subject must not be null`() {
        getCharacterDetails = GetCharacterDetails(charactersRepository)

        assertNotNull(getCharacterDetails)
    }

    @Test
    fun `should get character details and then return the mapped response`() {
        val id = 1

        val response = CharacterDetailsModel(
            id,
            "name",
            "status",
            "species",
            "type",
            "gender",
            "image",
            "origin",
            "location",
            listOf(
                EpisodeModel(1, "name1", "airDate1", "number1"),
                EpisodeModel(2, "name2", "airDate2", "number2")
            )
        )

        val result = CharacterDetails(
            id,
            "name",
            "status",
            "species",
            "image",
            "gender",
            "origin",
            "type",
            "location",
            listOf(
                Episode(1, "name1", "airDate1", "number1"),
                Episode(2, "name2", "airDate2", "number2")
            )
        )

        every { charactersRepository.getCharacterDetails(id) } returns Single.just(response)

        getCharacterDetails(GetCharacterDetails.Params(id))
            .test()
            .assertResult(result)
            .assertNoErrors()

        verify(exactly = 1) {
            charactersRepository.getCharacterDetails(id)
        }
        confirmVerified(charactersRepository)
    }
}
