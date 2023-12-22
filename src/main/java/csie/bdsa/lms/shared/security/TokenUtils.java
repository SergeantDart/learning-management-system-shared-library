package csie.bdsa.lms.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {

    @Value("${token.secret}")
    private String secret;

    public Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean isExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date(System.currentTimeMillis()));
        } catch (Exception ex) {
            return true;
        }
    }

    public String getUsername(String token) {
        try {
            return getClaims(token).getSubject();
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsername(token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }
}
