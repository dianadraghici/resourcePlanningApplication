package dania.app.web.security;

import java.util.Arrays;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dania.app.web.entities.UserEntity;
import dania.app.web.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

   private final UserRepository repository;

   public UserService(UserRepository repository) {
       this.repository = repository;
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       UserEntity user = repository.findByEmail(username);
       GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
       return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Arrays.asList(authority));
   }   
}