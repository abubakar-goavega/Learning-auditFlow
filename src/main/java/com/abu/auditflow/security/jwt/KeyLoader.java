package com.abu.auditflow.security.jwt;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class KeyLoader {

        private final JwtProperties properties;

        public KeyLoader(JwtProperties properties) {
                this.properties = properties;
        }

        public PrivateKey loadPrivateKey() {
                try {
                        String key = Files.readString(
                                        Path.of(properties.privateKeyPath()),
                                        StandardCharsets.UTF_8);

                        key = key
                                        .replace("-----BEGIN PRIVATE KEY-----", "")
                                        .replace("-----END PRIVATE KEY-----", "")
                                        .replaceAll("\\s", "");

                        byte[] decoded = Base64.getDecoder().decode(key);

                        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

                        return KeyFactory
                                        .getInstance("RSA")
                                        .generatePrivate(spec);

                } catch (Exception ex) {
                        throw new IllegalStateException("Failed to load private key", ex);
                }
        }

        public PublicKey loadPublicKey() {
                try {
                        String key = Files.readString(
                                        Path.of(properties.publicKeyPath()),
                                        StandardCharsets.UTF_8);

                        key = key
                                        .replace("-----BEGIN PUBLIC KEY-----", "")
                                        .replace("-----END PUBLIC KEY-----", "")
                                        .replaceAll("\\s", "");

                        byte[] decoded = Base64.getDecoder().decode(key);

                        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

                        return KeyFactory
                                        .getInstance("RSA")
                                        .generatePublic(spec);

                } catch (Exception ex) {
                        throw new IllegalStateException("Failed to load public key", ex);
                }
        }
}