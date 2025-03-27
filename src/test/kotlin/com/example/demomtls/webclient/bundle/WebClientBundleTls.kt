package com.example.demomtls.webclient.bundle

import com.example.demomtls.utils.TestProfiles
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(TestProfiles.TLS)
class WebClientBundleTls : WebClientBundleBase()
