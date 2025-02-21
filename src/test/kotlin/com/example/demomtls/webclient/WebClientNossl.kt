package com.example.demomtls.webclient

import com.example.demomtls.utils.Profiles
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(Profiles.NO_SSL)
class WebClientNossl : WebClientBase()
