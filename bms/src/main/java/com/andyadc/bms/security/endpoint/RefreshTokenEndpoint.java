package com.andyadc.bms.security.endpoint;

import com.andyadc.bms.entity.User;
import com.andyadc.bms.security.UserService;
import com.andyadc.bms.security.auth.jwt.extractor.TokenExtractor;
import com.andyadc.bms.security.auth.jwt.verifier.TokenVerifier;
import com.andyadc.bms.security.config.JwtSettings;
import com.andyadc.bms.security.config.WebSecurityConfig;
import com.andyadc.bms.security.exception.InvalidJwtTokenException;
import com.andyadc.bms.security.model.UserContext;
import com.andyadc.bms.security.model.token.JwtToken;
import com.andyadc.bms.security.model.token.JwtTokenFactory;
import com.andyadc.bms.security.model.token.RawAccessJwtToken;
import com.andyadc.bms.security.model.token.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RefreshTokenEndpoint {

    private JwtTokenFactory tokenFactory;
    private JwtSettings jwtSettings;
    private UserService userService;
    private TokenVerifier tokenVerifier;
    private TokenExtractor tokenExtractor;

    @RequestMapping(value = "/api/auth/token", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME));

        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey()).orElseThrow(() -> new InvalidJwtTokenException("Invalid jwt token: " + tokenPayload));

        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtTokenException("Invalid jwt token: " + tokenPayload);
        }

        String subject = refreshToken.getSubject();
        User user = userService.findByUsername(subject).orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));

        if (user.getAuthorities() == null) throw new InsufficientAuthenticationException("User has no roles assigned");

        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        UserContext userContext = UserContext.create(user.getUsername(), grantedAuthorities);

        return tokenFactory.createAccessJwtToken(userContext);
    }


    @Autowired
    public void setTokenFactory(JwtTokenFactory tokenFactory) {
        this.tokenFactory = tokenFactory;
    }

    @Autowired
    public void setJwtSettings(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTokenVerifier(TokenVerifier tokenVerifier) {
        this.tokenVerifier = tokenVerifier;
    }

    @Autowired
    public void setTokenExtractor(TokenExtractor tokenExtractor) {
        this.tokenExtractor = tokenExtractor;
    }
}
