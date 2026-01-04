package com.neit.hoccode.service;

import com.neit.hoccode.dto.request.IntrospectRequest;
import com.neit.hoccode.dto.request.LoginRequest;
import com.neit.hoccode.dto.request.LogoutRequest;
import com.neit.hoccode.dto.request.RefreshRequest;
import com.neit.hoccode.dto.response.AuthenticationResponse;
import com.neit.hoccode.dto.response.IntrospectResponse;
import com.neit.hoccode.entity.InvalidatedToken;
import com.neit.hoccode.entity.User;
import com.neit.hoccode.exception.AppException;
import com.neit.hoccode.exception.ErrorCode;
import com.neit.hoccode.repository.InvalidatedRepository;
import com.neit.hoccode.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthenticationService {
    @Value("${secret.key}")
    private String SIGNER_KEY;
    @Value("${token-valid-duration}")
    private long VALID_DURATION;
    @Value("${token-refresh-duration}")
    private long REFRESH_DURATION;

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    InvalidatedRepository invalidatedRepository;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository, InvalidatedRepository invalidatedRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.invalidatedRepository = invalidatedRepository;
    }

    public AuthenticationResponse authenticate(LoginRequest request){
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean authenticated =  passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(user);
        return AuthenticationResponse.builder().token(token).build();
    }
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        boolean isVerifiled = true;
        try {
            verifyToken(request.getToken(), false);
        }catch (AppException e){
            isVerifiled = false;
        }
        return IntrospectResponse.builder().verified(isVerifiled).build();
    }
    public void logout (LogoutRequest request) throws ParseException, JOSEException {
        try{
            var signToken = verifyToken(request.getToken(), true);

            String jwtId = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            invalidatedRepository.save(InvalidatedToken.builder()
                    .id(jwtId)
                    .expiryTime(expiryTime)
                    .build());
        } catch (AppException e) {
            log.info("Token already expired");
        }

    }
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signJWT = verifyToken(request.getToken(), true);

        var jwtId = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        invalidatedRepository.save(InvalidatedToken.builder()
                .id(jwtId)
                .expiryTime(expiryTime)
                .build());

        var username = signJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        var token = generateToken(user);
        return AuthenticationResponse.builder().token(token).build();

    }
    private String generateToken(User user){

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("hoccode.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token", e);
            throw new AppException(ErrorCode.INVALID_KEY);
        }
    }
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);

        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expTime;
        if (isRefresh){
            expTime = new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                    .plus(REFRESH_DURATION, ChronoUnit.SECONDS).toEpochMilli());
        }else {
            expTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        }
        var verified = signedJWT.verify(verifier);

        if (!(verified && expTime.after(new Date())))
            throw new AppException(ErrorCode.TOKEN_INVALID);

        if(invalidatedRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
                throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

}
