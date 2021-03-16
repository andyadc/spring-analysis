package com.andyadc.bms.security.auth.ajax;

import com.andyadc.bms.entity.User;
import com.andyadc.bms.security.UserService;
import com.andyadc.bms.security.data.DatabaseUserService;
import com.andyadc.bms.security.model.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public AjaxAuthenticationProvider(PasswordEncoder passwordEncoder, DatabaseUserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        User user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }

        if (user.getAuthorities() == null) {
            throw new InsufficientAuthenticationException("User has no authorities assigned");
        }

        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        UserContext context = UserContext.create(username, grantedAuthorities);
        return new UsernamePasswordAuthenticationToken(context, null, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(clazz));
    }
}
