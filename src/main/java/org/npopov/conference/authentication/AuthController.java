package org.npopov.conference.authentication;

import org.npopov.conference.person.Person;
import org.npopov.conference.person.PersonAuthRequest;
import org.npopov.conference.person.PersonDTO;
import org.npopov.conference.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private PersonService personService;

    @PostMapping("/register")
    public Map<String, Object> createPerson(@Valid @RequestBody PersonDTO personInput) {
        Person user = personService.createPerson(personInput);
        String token = jwtUtil.generateToken(user.getUsername());
        return Collections.singletonMap("token", token);
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@Valid @RequestBody PersonAuthRequest body){
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());

            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(body.getUsername());

            return Collections.singletonMap("token", token);
        }catch (AuthenticationException authExc){
            throw new RuntimeException("Invalid Login Credentials");
        }
    }
}
