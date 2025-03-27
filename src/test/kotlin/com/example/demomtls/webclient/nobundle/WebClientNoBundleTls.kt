package com.example.demomtls.webclient.nobundle

import com.example.demomtls.utils.TestProfiles
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(TestProfiles.TLS)
class WebClientNoBundleTls : WebClientNoBundleBase()
