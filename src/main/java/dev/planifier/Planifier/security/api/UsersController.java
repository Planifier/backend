package dev.planifier.Planifier.security.api;

import dev.planifier.Planifier.exception.UserWithEmailAlreadyExistsException;
import dev.planifier.Planifier.security.AuthenticationService;
import dev.planifier.Planifier.security.users.UsersService;
import dev.planifier.Planifier.security.users.model.User;
import dev.planifier.Planifier.security.users.model.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UsersController {

    @Autowired
    private UsersService usersService;
    @Autowired
    private AuthenticationService authenticationService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@RequestBody UserInfo userInfo) {
        try {
            usersService.registerNewUser(userInfo.name(), userInfo.email(), encoder.encode(userInfo.password()));
            return ResponseEntity.ok().build();
        } catch (UserWithEmailAlreadyExistsException _) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(HttpServletRequest req, @RequestParam String email, @RequestParam String password) {
        return authenticationService.authenticateUser(req, email, password) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserId() {
        User loggedUser = getCurrentUserDetails();
        if (loggedUser == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(loggedUser.getId());
    }

    private User getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) return (User) principal;
        }
        return null;
    }

}
