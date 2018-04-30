package com.locationtracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locationtracker.model.User;
import com.locationtracker.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import netscape.javascript.JSObject;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.locationtracker.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;

        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(LOG_IN_URL,"POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            User creds = new ObjectMapper()
                    .readValue(req.getInputStream(), User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        String username = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username);


        String token = Jwts.builder()
                .setSubject(username)
                .claim("password", user.getPassword())
                .claim("id", user.getId())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

        JSONObject responseObject = new JSONObject();
        try {
            responseObject.put("id", user.getId());
            responseObject.put("username", user.getUsername());
            responseObject.put("removed", user.isRemoved());
            responseObject.put("token", token);

        }catch (Exception e){

        }

        res.setContentType("application/json");
        ServletOutputStream response = res.getOutputStream();
        response.print(responseObject.toString());
    }
}