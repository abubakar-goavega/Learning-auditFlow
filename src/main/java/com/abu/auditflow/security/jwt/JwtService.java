package com.abu.auditflow.security.jwt;

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.crypto.RSASSAVerifier;

@Service
public class JwtService {

        private final JwtProperties properties;
        private final KeyLoader keyLoader;

        public JwtService(
                        JwtProperties properties,
                        KeyLoader keyLoader) {

                this.properties = properties;
                this.keyLoader = keyLoader;
        }

        public String generateAccessToken(
                        Long userId,
                        String username,
                        String role) {

                try {

                        Instant now = Instant.now();

                        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                                        .issuer(properties.issuer())
                                        .subject(username)
                                        .claim("role", role)
                                        .claim("userId", userId)
                                        .issueTime(Date.from(now))
                                        .expirationTime(Date.from(now.plus(
                                                        properties.accessTokenExpirationMinutes(),
                                                        ChronoUnit.MINUTES)))
                                        .build();
                        SignedJWT jwt = new SignedJWT(
                                        new JWSHeader(JWSAlgorithm.RS256),claims);

                        jwt.sign(new RSASSASigner(keyLoader.loadPrivateKey()));

                        return jwt.serialize();

                } catch (Exception ex) {
                        throw new IllegalStateException("Failed to generate token",ex);
                }
        }

        private SignedJWT parseToken(String token) {
                try {
                        return SignedJWT.parse(token);
                } catch (Exception ex) {
                        throw new IllegalArgumentException("Invalid JWT", ex);
                }
        }

        public String extractUsername(
                        String token) {

                try {

                        return parseToken(token)
                                        .getJWTClaimsSet()
                                        .getSubject();

                } catch (Exception ex) {

                        throw new IllegalStateException(
                                        "Failed to extract username",
                                        ex);
                }
        }

        public long getAccessTokenExpirationMinutes() {
                return properties.accessTokenExpirationMinutes();
        }

        public String extractRole(
                        String token) {

                try {

                        return parseToken(token)
                                        .getJWTClaimsSet()
                                        .getStringClaim("role");

                } catch (Exception ex) {

                        throw new IllegalStateException(
                                        "Failed to extract role",
                                        ex);
                }
        }

        public boolean isTokenValid(
                        String token) {

                try {

                        SignedJWT jwt = SignedJWT.parse(token);

                        boolean signatureValid = jwt.verify(

                                        new RSASSAVerifier(
                                                        (RSAPublicKey) keyLoader.loadPublicKey()));

                        if (!signatureValid) {
                                return false;
                        }

                        Instant expiration = jwt.getJWTClaimsSet()
                                        .getExpirationTime()
                                        .toInstant();

                        return expiration.isAfter(
                                        Instant.now());

                } catch (Exception ex) {

                        return false;
                }
        }

        public Instant extractExpiration(
                        String token) {

                try {

                        return parseToken(token)
                                        .getJWTClaimsSet()
                                        .getExpirationTime()
                                        .toInstant();

                } catch (Exception ex) {

                        throw new IllegalStateException(
                                        "Failed to extract expiration",
                                        ex);
                }
        }
}