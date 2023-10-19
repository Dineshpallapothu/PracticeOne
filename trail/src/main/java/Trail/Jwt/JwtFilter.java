package Trail.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver exceptionResolver;
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    UserDetailsService userDetailsService;
//    @Autowired
//    UserDetails userDetails;

    @Autowired
    public JwtFilter(HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestheader=request.getHeader("Authorization");
        String token=null;
        String username=null;

        if(requestheader!=null &&requestheader.startsWith("Bearer")){
            token=requestheader.substring(7);
            try {
                username=jwtHelper.extractUsername(token);
            }
            catch (Exception e){
                exceptionResolver.resolveException(request,response,null,e);
            }
        }
        else {
            log.info("validation failed..........");
        }
        if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails1=userDetailsService.loadUserByUsername(username);
                boolean validate=jwtHelper.validate(token,userDetails1);
                if(validate){
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails1,null,userDetails1.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                else {
                    log.info("Validation Failed");
                }

        }
        filterChain.doFilter(request,response);

    }
}
