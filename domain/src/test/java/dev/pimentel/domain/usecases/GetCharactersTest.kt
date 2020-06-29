package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.Character
import dev.pimentel.domain.models.CharacterModel
import dev.pimentel.domain.models.PagedResponse
import dev.pimentel.domain.repositories.CharactersRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetCharactersTest {

    private val charactersRepository = mockk<CharactersRepository>()
    private lateinit var getCharacters: GetCharacters

    @Test
    @BeforeEach
    fun `subject must not be null`() {
        getCharacters = GetCharacters(charactersRepository)

        assertNotNull(getCharacters)
    }

    @Test
    fun `should get characters and then return the mapped response`() {
        val page = 1
        val name = "name"
        val species = "species"
        val status = "status"
        val gender = "gender"

        val response = PagedResponse(
            10,
            listOf(
                CharacterModel(1, "name1", "status1", "image1"),
                CharacterModel(2, "name2", "status2", "image2")
            )
        )

        val result = GetCharacters.Response(
            10,
            listOf(
                Character(1, "name1", "status1", "image1"),
                Character(2, "name2", "status2", "image2")
            )
        )

        every {
            charactersRepository.getCharacters(page, name, species, status, gender)
        } returns Single.just(response)

        getCharacters(GetCharacters.Params(page, name, species, status, gender))
            .test()
            .assertResult(result)
            .assertNoErrors()

        verify(exactly = 1) {
            charactersRepository.getCharacters(page, name, species, status, gender)
        }
        confirmVerified(charactersRepository)
    }
}
