package com.example.demomtls.javahttpclient

import com.example.demomtls.utils.Profiles
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(Profiles.TLS)
class JavaHttpClientTls : JavaHttpClientBase()
