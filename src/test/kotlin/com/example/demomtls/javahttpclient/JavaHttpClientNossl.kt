package com.example.demomtls.javahttpclient

import com.example.demomtls.utils.Profiles
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(Profiles.NO_SSL)
class JavaHttpClientNossl : JavaHttpClientBase()
