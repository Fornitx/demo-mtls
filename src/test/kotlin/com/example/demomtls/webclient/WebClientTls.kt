package com.example.demomtls.webclient

import com.example.demomtls.utils.Profiles
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(Profiles.TLS)
class WebClientTls : WebClientBase()
