package vn.ibex.digital_blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ibex.digital_blog.domain.User;
import vn.ibex.digital_blog.domain.response.ResLoginDTO;
import vn.ibex.digital_blog.domain.response.ResUserDTO;
import vn.ibex.digital_blog.service.UserService;
import vn.ibex.digital_blog.util.SecurityUtil;
import vn.ibex.digital_blog.util.annotation.ApiMessage;

// Update Profile
// Change Password
// Read profile


@RestController
@RequestMapping("/api/v1")
public class AuthController {
    UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
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

    
}
