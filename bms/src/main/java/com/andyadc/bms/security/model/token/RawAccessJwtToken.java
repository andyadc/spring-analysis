package com.andyadc.bms.security.model.token;

import com.andyadc.bms.security.exception.JwtExpiredTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class RawAccessJwtToken implements JwtToken {

    private static final Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);

    private final String token;

    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    /**
     * Parses and validates JWT Token signature.
     */
    public Jws<Claims> parseClaims(String signingKey) {
        try {
            Key key = new SecretKeySpec(signingKey.getBytes(), SignatureAlgorithm.HS512.getJcaName());
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(this.token);
//            return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SecurityException ex) {
            logger.error("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            logger.info("JWT Token is expired", expiredEx);
            throw new JwtExpiredTokenException(this, "JWT Token expired", expiredEx);
        } catch (SignatureException e) {
            logger.error("JWT signature does not match", e);
            throw new BadCredentialsException("Invalid JWT token: ", e);
        }
    }

    @Override
    public String getToken() {
        return token;
    }
}
