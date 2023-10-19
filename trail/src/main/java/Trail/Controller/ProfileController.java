package Trail.Controller;

import Trail.Entities.Profile;
import Trail.Entities.Requestone;
import Trail.Jwt.JwtHelper;
import Trail.service.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtHelper jwtHelper;

    @GetMapping("/all")
    public List<Profile> getall(){
        return profileService.getAll();
    }
    @PostMapping("/save")
    public Profile create(@RequestBody Profile profile){
        return profileService.create(profile);
    }

    @PostMapping("/login")
    public String login(@RequestBody Requestone requestone){
        UserDetails userDetails=profileService.loadUserByUsername(requestone.getEmail());
        try {
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(requestone.getEmail(),requestone.getPassword());
            authenticationManager.authenticate(authenticationToken);
        }
        catch (BadCredentialsException e){
            throw  new BadCredentialsException("Bad Credentials");
        }

    return jwtHelper.generate(userDetails);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public String handleone(BadCredentialsException e){
        return e.getMessage();}


}
