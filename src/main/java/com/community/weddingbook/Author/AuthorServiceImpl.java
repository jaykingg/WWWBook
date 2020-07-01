package com.community.weddingbook.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService, UserDetailsService {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    /* Security */
    /**************************************************************************************/
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Author author = authorRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException(id));

        return new User(author.getId(),author.getPassword(),authorities(author.getRoles()));
    }

    private Collection<? extends GrantedAuthority> authorities(Set<AuthorRole> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                //.map(r -> new SimpleGrantedAuthority(r.name()))
                .collect(Collectors.toSet());
    }
    /**************************************************************************************/

    @Override
    public Author saveAuthor(Author author) {
        author.setPassword(this.passwordEncoder.encode(author.getPassword()));
        return this.authorRepository.save(author);
    }

}
