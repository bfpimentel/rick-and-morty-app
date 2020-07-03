package dev.pimentel.data.repository.episodes

import dev.pimentel.data.models.EpisodeModel
import dev.pimentel.data.models.PagedResponse
import dev.pimentel.data.repositories.episodes.EpisodesRepositoryImpl
import dev.pimentel.data.sources.remote.EpisodesRemoteDataSource
import dev.pimentel.domain.repositories.EpisodesRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import dev.pimentel.domain.models.EpisodeModel as DomainEpisodeModel
import dev.pimentel.domain.models.PagedResponse as DomainPagedResponse

class EpisodesRepositoryTest {

    private val episodesRemoteDataSource = mockk<EpisodesRemoteDataSource>()
    private lateinit var episodesRepository: EpisodesRepository

    @Test
    @BeforeEach
    fun `subject must not be null`() {
        episodesRepository = EpisodesRepositoryImpl(episodesRemoteDataSource)

        assertNotNull(episodesRepository)
    }

    @Test
    fun `should get locations`() {
        val dataResponse = PagedResponse(
            PagedResponse.Info(10),
            listOf(
                EpisodeModel(1, "name1", "type1", "number1"),
                EpisodeModel(2, "name2", "type2", "number2")
            )
        )

        val domainResponse = DomainPagedResponse(
            10,
            listOf(
                DomainEpisodeModel(1, "name1", "type1", "number1"),
                DomainEpisodeModel(2, "name2", "type2", "number2")
            )
        )

        val page = 1
        val name = "name"
        val number = "number"

        every {
            episodesRemoteDataSource.getEpisodes(page, name, number)
        } returns Single.just(dataResponse)

        episodesRepository.getEpisodes(page, name, number)
            .test()
            .assertResult(domainResponse)
            .assertNoErrors()

        verify(exactly = 1) {
            episodesRemoteDataSource.getEpisodes(page, name, number)
        }
        confirmVerified(episodesRemoteDataSource)
    }
}
