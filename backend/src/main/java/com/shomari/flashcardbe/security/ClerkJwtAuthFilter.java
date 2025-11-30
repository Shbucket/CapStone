//package com.shomari.flashcardbe.security;
//
//import com.nimbusds.jose.jwk.source.RemoteJWKSet;
//import com.nimbusds.jose.proc.SecurityContext;
//import com.nimbusds.jwt.SignedJWT;
//import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
//import com.nimbusds.jwt.proc.DefaultJWTProcessor;
//import com.nimbusds.jwt.proc.JWTProcessor;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.proc.JWSKeySelector;
//import com.nimbusds.jose.proc.JWSVerificationKeySelector;
//import com.nimbusds.jose.JWSAlgorithm;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.net.URL;
//import java.text.ParseException;
//import java.util.Collections;
//import java.util.List;
//
//public class ClerkJwtAuthFilter extends OncePerRequestFilter {
//
//    private final String jwksUrl;
//    private final String issuer;
//
//    public ClerkJwtAuthFilter(String issuer, String jwksUrl) {
//        this.issuer = issuer;
//        this.jwksUrl = jwksUrl;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//
//            try {
//                SignedJWT signedJWT = SignedJWT.parse(token);
//
//                // Verify issuer
//                if (!issuer.equals(signedJWT.getJWTClaimsSet().getIssuer())) {
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    return;
//                }
//
//                // Fetch JWKs from Clerk
//                JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(new URL(jwksUrl));
//                ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
//                JWSKeySelector<SecurityContext> keySelector =
//                        new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource);
//                jwtProcessor.setJWSKeySelector(keySelector);
//
//                jwtProcessor.process(signedJWT, null);
//
//                // If we reach here, JWT is valid
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(
//                                signedJWT.getJWTClaimsSet().getSubject(),
//                                null,
//                                List.of(new SimpleGrantedAuthority("USER"))
//                        );
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//
//            } catch (ParseException e) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            } catch (Exception e) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}