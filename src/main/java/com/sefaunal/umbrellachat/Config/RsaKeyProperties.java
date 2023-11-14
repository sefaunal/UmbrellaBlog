package com.sefaunal.umbrellachat.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author github.com/sefaunal
 * @since 2023-09-17
 **/

@ConfigurationProperties(prefix = "rsa")
public record RsaKeyProperties(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
}
