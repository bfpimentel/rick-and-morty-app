package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.Episode
import dev.pimentel.domain.models.EpisodeModel
import dev.pimentel.domain.models.PagedResponse
import dev.pimentel.domain.repositories.EpisodesRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetEpisodesTest {

    private val episodesRepository = mockk<EpisodesRepository>()
    private lateinit var getEpisodes: GetEpisodes

    @Test
    @BeforeEach
    fun `subject must not be null`() {
        getEpisodes = GetEpisodes(episodesRepository)

        assertNotNull(getEpisodes)
    }

    @Test
    fun `should get episodes and then return the mapped response`() {
        val page = 1
        val name = "name"
        val number = "number"

        val response = PagedResponse(
            10,
            listOf(
                EpisodeModel(1, "name1", "airDate1", "number1"),
                EpisodeModel(2, "name2", "airDate2", "number2")
            )
        )

        val result = GetEpisodes.Response(
            10,
            listOf(
                Episode(1, "name1", "airDate1", "number1"),
                Episode(2, "name2", "airDate2", "number2")
            )
        )

        every {
            episodesRepository.getEpisodes(page, name, number)
        } returns Single.just(response)

        getEpisodes(GetEpisodes.Params(page, name, number))
            .test()
            .assertResult(result)
            .assertNoErrors()

        verify(exactly = 1) {
            episodesRepository.getEpisodes(page, name, number)
        }
        confirmVerified(episodesRepository)
    }
}
