package dev.pimentel.data.repository.characters

import dev.pimentel.data.models.CharacterDetailsModel
import dev.pimentel.data.models.CharacterModel
import dev.pimentel.data.models.EpisodeModel
import dev.pimentel.data.models.PagedResponse
import dev.pimentel.data.repositories.characters.CharactersRepositoryImpl
import dev.pimentel.data.sources.remote.CharactersRemoteDataSource
import dev.pimentel.data.sources.remote.EpisodesRemoteDataSource
import dev.pimentel.domain.repositories.CharactersRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import dev.pimentel.domain.models.CharacterDetailsModel as DomainCharacterDetailsModel
import dev.pimentel.domain.models.CharacterModel as DomainCharacterModel
import dev.pimentel.domain.models.EpisodeModel as DomainEpisodeModel
import dev.pimentel.domain.models.PagedResponse as DomainPagedResponse

class CharactersRepositoryTest {

    private val charactersRemoteDataSource = mockk<CharactersRemoteDataSource>()
    private val episodesRemoteDataSource = mockk<EpisodesRemoteDataSource>()
    private lateinit var charactersRepository: CharactersRepository

    @Test
    @BeforeEach
    fun `subject must not be null`() {
        charactersRepository = CharactersRepositoryImpl(
            charactersRemoteDataSource,
            episodesRemoteDataSource
        )

        assertNotNull(charactersRepository)
    }

    @Test
    fun `should get characters`() {
        val dataResponse = PagedResponse(
            PagedResponse.Info(10),
            listOf(
                CharacterModel(1, "name1", "status1", "image1"),
                CharacterModel(2, "name2", "status2", "image2")
            )
        )

        val domainResponse = DomainPagedResponse(
            10,
            listOf(
                DomainCharacterModel(1, "name1", "status1", "image1"),
                DomainCharacterModel(2, "name2", "status2", "image2")
            )
        )

        val page = 1
        val name = "name"
        val species = "species"
        val status = "status"
        val gender = "gender"

        every {
            charactersRemoteDataSource.getCharacters(page, name, species, status, gender)
        } returns Single.just(dataResponse)

        charactersRepository.getCharacters(page, name, species, status, gender)
            .test()
            .assertResult(domainResponse)
            .assertNoErrors()

        verify(exactly = 1) {
            charactersRemoteDataSource.getCharacters(page, name, species, status, gender)
        }
        confirmVerified(charactersRemoteDataSource, episodesRemoteDataSource)
    }

    @Test
    fun `should return character details`() {
        val id = 1

        val dataEpisodeUrl1 = "episode1"
        val dataEpisodeUrl2 = "episode2"
        val dataEpisodeUrl3 = "episode3"

        val dataEpisode1 = EpisodeModel(1, "episode1", "airDate1", "number1")
        val dataEpisode2 = EpisodeModel(2, "episode2", "airDate2", "number2")
        val dataEpisode3 = EpisodeModel(3, "episode3", "airDate3", "number3")

        val dataResponse = CharacterDetailsModel(
            id,
            "name",
            "status",
            "species",
            "type",
            "gender",
            CharacterDetailsModel.Origin("origin", "originUrl"),
            CharacterDetailsModel.Location("location", "locationUrl"),
            listOf(dataEpisodeUrl1, dataEpisodeUrl2, dataEpisodeUrl3),
            "image"
        )

        val domainEpisode1 = DomainEpisodeModel(1, "episode1", "airDate1", "number1")
        val domainEpisode2 = DomainEpisodeModel(2, "episode2", "airDate2", "number2")
        val domainEpisode3 = DomainEpisodeModel(3, "episode3", "airDate3", "number3")

        val domainResponse = DomainCharacterDetailsModel(
            id,
            "name",
            "status",
            "species",
            "type",
            "gender",
            "image",
            "origin",
            "location",
            listOf(domainEpisode1, domainEpisode2, domainEpisode3)
        )

        every {
            charactersRemoteDataSource.getCharacterDetails(id)
        } returns Single.just(dataResponse)
        every {
            episodesRemoteDataSource.getEpisode(dataEpisodeUrl1)
        } returns Single.just(dataEpisode1)
        every {
            episodesRemoteDataSource.getEpisode(dataEpisodeUrl2)
        } returns Single.just(dataEpisode2)
        every {
            episodesRemoteDataSource.getEpisode(dataEpisodeUrl3)
        } returns Single.just(dataEpisode3)

        charactersRepository.getCharacterDetails(id)
            .test()
            .assertResult(domainResponse)
            .assertNoErrors()

        verify(exactly = 1) {
            charactersRemoteDataSource.getCharacterDetails(id)
            episodesRemoteDataSource.getEpisode(dataEpisodeUrl1)
            episodesRemoteDataSource.getEpisode(dataEpisodeUrl2)
            episodesRemoteDataSource.getEpisode(dataEpisodeUrl3)
        }
        confirmVerified(charactersRemoteDataSource, episodesRemoteDataSource)
    }
}
