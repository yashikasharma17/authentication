package com.example.authentication.controller;

import com.example.authentication.service.Profileservice;
import com.example.authentication.util.jwtutil;
import com.example.authentication.io.Authrequest;
import com.example.authentication.io.Authresponse;
import com.example.authentication.service.Appuserdetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private Appuserdetails appuserdetails;
    @Autowired
    private jwtutil jwt;
    @Autowired
    private Profileservice ps;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Authrequest request) {
        try {
            authenticate(request.getPassword(), request.getEmail());
            final UserDetails userDetails = appuserdetails.loadByUserName(request.getEmail());
            final String jwtToken = jwt.generateToken(userDetails);
            ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Strict")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new Authresponse(request.getEmail(),jwtToken));
        } catch (BadCredentialsException ex) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", true);
            map.put("message", "Email or password is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } catch (DisabledException ex) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", true);
            map.put("message", "Account is Disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        } catch (Exception ex) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", true);
            map.put("message", "Authorization failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }
    }

    private void authenticate(String password, String email) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @GetMapping("./is_authenticated")
    public ResponseEntity<Boolean> isauntheticated(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        return ResponseEntity.ok(email != null);
    }
    @PostMapping("./send-resend-otp")
    public void sendResendOTP(@RequestParam String email){
        try{
            ps.sendResendOTP(email);
        }
        catch(Exception e){
throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }
}