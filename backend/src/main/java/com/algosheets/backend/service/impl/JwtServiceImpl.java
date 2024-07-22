package com.algosheets.backend.service.impl;

import com.algosheets.backend.exception.TokenException;
import com.algosheets.backend.repository.AuthRepository;

import com.algosheets.backend.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
//    openssl rand -base64 32

    @Autowired
    AuthRepository authRepository;



    @Override
    public String generateToken(Map<String, Object> extraClaims, UUID userId){
//        System.out.println("hello "+SECRET_KEY);

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userId.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60*60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    @Override
    public String generateToken(UUID userId){
        return generateToken(new HashMap<>(),userId);
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    @Override
    public String extractUserId(String jwtToken) {
        return extractClaim(jwtToken,Claims::getSubject);
    }

    public Date extractExpiration(String jwtToken ){
        return  extractClaim(jwtToken,Claims::getExpiration);
    }

    private <T> T extractClaim(String jwtToken, Function<Claims,T> claimsResolver){

        Claims claims=extractAllClaims(jwtToken);

        return claimsResolver.apply(claims);

    }
    private Claims extractAllClaims(String jwtToken){
//        System.out.println(SECRET_KEY);
        try {
            return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(jwtToken).getBody();
        }catch (Exception e){

            throw new TokenException("invalid token");
        }
    }


    @Override
    public Boolean isTokenValid(String jwtToken,UUID userId){
        return !isTokenExpired(jwtToken) && isCodeValid(userId);
    }

    private boolean isCodeValid(UUID userId) {
        if(authRepository.findByUserId(userId).isPresent()) return true;
        else throw new TokenException("invalid code");
    }

    private boolean isTokenExpired(String jwtToken) {
        if(extractExpiration(jwtToken).before(new Date())) {
            throw new TokenException("token expired");
        }
        return false;
    }

}
