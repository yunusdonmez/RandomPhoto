package com.genesis.randomphoto.network.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.genesis.randomphoto.dto.PhotoDTO

@JsonIgnoreProperties(ignoreUnknown = true)
data class PhotoListResponse(val list: PhotoDTO)
