package vn.fpt.diamond_shop.security.endpoint;

import vn.fpt.diamond_shop.controller.BaseController;
import vn.fpt.diamond_shop.request.LoginRequest;
import vn.fpt.diamond_shop.security.TokenProvider;
import vn.fpt.diamond_shop.security.UserPrincipal;
import vn.fpt.diamond_shop.security.response.AuthResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/shop/auth")
public class AuthController extends BaseController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) userPrincipal.getAuthorities();
        String role = authorities.iterator().next().getAuthority();

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token, role));
    }
}

