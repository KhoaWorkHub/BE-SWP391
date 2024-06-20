package vn.fpt.diamond_shop.security.endpoint;

import org.springframework.http.HttpStatus;
<<<<<<< HEAD
import org.springframework.web.server.ResponseStatusException;
import vn.fpt.diamond_shop.controller.BaseController;
import vn.fpt.diamond_shop.repository.UserRepository;
import vn.fpt.diamond_shop.request.LoginRequest;
import vn.fpt.diamond_shop.security.TokenProvider;
import vn.fpt.diamond_shop.security.UserPrincipal;
import vn.fpt.diamond_shop.security.response.AuthResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
=======
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;
import vn.fpt.diamond_shop.controller.BaseController;
import vn.fpt.diamond_shop.repository.UserRepository;
import vn.fpt.diamond_shop.security.AccountService;
import vn.fpt.diamond_shop.response.AuthResponse;
import vn.fpt.diamond_shop.request.LoginRequest;
import vn.fpt.diamond_shop.request.SignUpRequest;
import vn.fpt.diamond_shop.security.TokenProvider;
>>>>>>> origin/Khang
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
<<<<<<< HEAD
=======
    private AccountService accountService;
    @Autowired
>>>>>>> origin/Khang
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;

<<<<<<< HEAD
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userRepository.findById(userPrincipal.getId()).get().isActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account is deaactive");
        }

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) userPrincipal.getAuthorities();
        String role = authorities.iterator().next().getAuthority();

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token, role));
    }
}

=======
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        accountService.register(signUpRequest);

        return ok("User registered successfully");
    }

}
>>>>>>> origin/Khang
