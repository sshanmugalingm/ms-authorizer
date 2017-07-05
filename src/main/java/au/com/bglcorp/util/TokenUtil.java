package au.com.bglcorp.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Created by senthurshanmugalingm on 5/07/2017.
 */
public interface TokenUtil {

    static String constructToken(String subject, String salt) {
        return Jwts.builder()
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS512, salt)
                .compact();
    }

    static String destructToken(String salt, String cipheredToken) {
        return Jwts.parser()
                .setSigningKey(salt)
                .parseClaimsJws(cipheredToken)
                .getBody()
                .getSubject();
    }

}
