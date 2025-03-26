package QBank.LoginSignup.Config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {


    private final String secret = "SomethingIsSomethingSomethingOfNothingIsSomethingSomethingOfNothingIsStillSomethingNothingOfNothingIsNothingAndNothingIsNothing";

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        long expiration = 86400000;
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        String tokenUsername = getUsernameFromToken(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = getClaimsFromToken(token).getExpiration();
        return expirationDate.before(new Date());
    }

   /* public Claims validateToken(String token) throws SignatureException {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }*/
}
