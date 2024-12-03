package vn.ibex.digital_blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ibex.digital_blog.dto.model.User;
import vn.ibex.digital_blog.dto.model.response.ResCreateUserDTO;
import vn.ibex.digital_blog.dto.model.response.ResLoginDTO;
import vn.ibex.digital_blog.dto.model.response.ResUserDTO;
import vn.ibex.digital_blog.service.UserService;
import vn.ibex.digital_blog.util.SecurityUtil;
import vn.ibex.digital_blog.util.annotation.ApiMessage;
import vn.ibex.digital_blog.util.error.IdInvalidException;

// Update Profile
// Change Password
// Read profile


@RestController
@RequestMapping("/api")
public class AuthController {
    UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @GetMapping("/auth/account")
    @ApiMessage("Get account information")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount(){
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
        ? SecurityUtil.getCurrentUserLogin().get()
        : "";

        User  currUser = this.userService.handleGetUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if(currUser!=null){
            userLogin.setId(currUser.getId());
            userLogin.setEmail(currUser.getEmail());
            userLogin.setName(currUser.getFirstName() + " " + currUser.getLastName());
            userLogin.setRole(currUser.getRole());
        }
        return  ResponseEntity.ok(userLogin);
    }


    @PutMapping("/users")
    @ApiMessage("update a user")
    public ResponseEntity<ResCreateUserDTO>  updateUser(@RequestBody User user)
    throws IdInvalidException{

        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (email.equals("")) {
            throw new IdInvalidException("User is not found");
        }

        User currUser = this.userService.handleUpdateUser(user, email);
        if(currUser == null) {
            throw new IdInvalidException("User id: " + user.getId() + " is not found");
        }
        return ResponseEntity.ok(this.userService.convertToResUserDTO(currUser));
    }

    @PutMapping("/users/change-password")
    @ApiMessage("chang password")
    public ResponseEntity<Void>  changePassword(@RequestBody String newPassword)
    throws IdInvalidException{

        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (email.equals("")) {
            throw new IdInvalidException("User is not found");
        }
        User user = this.userService.handleGetUserByUsername(email);
        String hashPassword = this.passwordEncoder.encode(newPassword);
        user.setPassword(hashPassword);

        this.userService.handleSaveUser(user);
        return ResponseEntity.ok().build();
    }

    
}
