package com.example.authbackend.repo;

import com.example.authbackend.model.Userlogin;
import com.example.authbackend.repo.UserLoginRepo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.example.authbackend.model.Userlogin;
import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;

//import com.google.api.client.json.jackson2.JacksonFactory;



import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:3000")
public class Userlogincontroller {



	@Autowired
    private UserLoginRepo UserLoginRepo;
	
	@GetMapping("/")
    public String home() {
        return "API is running!";
    }

    @GetMapping("/userlogin")
    public List<Userlogin> getAllUsers() {
        return UserLoginRepo.findAll();
    }

    @PostMapping("/userlogin")
    public Userlogin addUser(@RequestBody Userlogin user) {
        return UserLoginRepo.save(user);
    }
    
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
        String idTokenString = body.get("token");

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList("google-oauth-id"))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String password = (String) payload.get("password");

                Userlogin user = UserLoginRepo.findByEmail(email).orElseGet(() -> {
                    Userlogin newUser = new Userlogin();
                    newUser.setEmail(email);
                    newUser.setPassword(password);
                    return UserLoginRepo.save(newUser);
                });

                return ResponseEntity.ok("Success");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Auth error");
        }
    }
}
