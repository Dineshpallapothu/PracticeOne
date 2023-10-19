package Trail.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JwtHelper {

    private static final long validity=1*60*60;
    private String secrete="YIQGN2s0es7oRRA6d6wBAhFdBEXcPpRRDpzswJbWSkodJ3ZL0onhnO3UpSUu5BJFVCaJ8GwVIwHUvUa4ktIlMGvNpHk5WWcVDFxjFK9TNG7f5AreFwin29Zf1zuhNF+AkZlaCKI1tuAYdndlz/PDKO0GiBPEmxcUeocnWHQrqaq3Y2VJB5f9wdTRhQTqlNnqyb06m3qNeAshhjzfkDjCl/wNwfawzNolV6pzn5J1qfOsshnMGFlzcBOPIO89SBds6cATWZRJPHeulPv9X4rvMLp3Z8z+xcQObCSX3iB76do2VRlmSnmBuMwvAa69kkYqOGv/2PlrEPU/yWPozvLw5o1CaHCdxUrLcnhWei/jKXU=";

    public String generate(UserDetails userDetails){
        Map<String, Objects> claims=new HashMap<>();
        return doGenerate(claims,userDetails.getUsername());
    }

    private String doGenerate(Map<String, Objects> claims, String username) {
        return Jwts.builder().setClaims(claims).signWith( SignatureAlgorithm.HS512,secrete).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+validity*100)).setSubject(username).compact();
    }
    public String extractUsername(String token){
        return getclaims(token,Claims::getSubject);
    }
    private Date exiprationDate(String token){
        return getclaims(token,Claims::getExpiration);
    }
    public Boolean validate(String token, UserDetails userDetails){
        String username=extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isExpired(token));
    }
    private boolean isExpired(String token){
        return exiprationDate(token).before(new Date());
    }

    private <T> T getclaims(String token,Function<Claims,T> claimeTFunction){
        Claims claims=getAllClaims(token);
       return claimeTFunction.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(secrete).parseClaimsJws(token).getBody();
    }


}
