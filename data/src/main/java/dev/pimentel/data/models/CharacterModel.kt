package dev.pimentel.data.models

import dev.pimentel.domain.models.CharacterModel as DomainCharacterModel

data class CharacterModel(
    private val _id: Int,
    private val _name: String,
    private val _status: String,
    private val _type: String,
    private val _gender: String,
    private val _origin: Origin,
    private val _location: Location,
    private val _episodes: List<String>,
    private val _image: String
) : DomainCharacterModel(
    _id,
    _name,
    _status,
    _type,
    _gender,
    _origin,
    _location,
    _episodes,
    _image
) {

    data class Origin(
        private val _name: String,
        private val _url: String
    ) : DomainCharacterModel.Origin(
        _name,
        _url
    )

    data class Location(
        private val _name: String,
        private val _url: String
    ) : DomainCharacterModel.Location(
        _name,
        _url
    )
}
