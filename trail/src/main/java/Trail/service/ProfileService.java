package Trail.service;

import Trail.Entities.Profile;
import Trail.Entities.Users;
import Trail.Repository.ProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService implements UserDetailsService {
    @Autowired
    ProfileRepo profileRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    public List<Profile>getAll(){
       return profileRepo.findAll();
    }
    public Profile create(Profile profile){
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        return profileRepo.save(profile);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Profile> profile=profileRepo.findByEmail(username);
        return profile.map(Users::new).orElseThrow(()->new RuntimeException("user not found"));
    }
}
