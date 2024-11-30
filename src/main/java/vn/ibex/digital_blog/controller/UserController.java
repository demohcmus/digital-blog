package vn.ibex.digital_blog.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ibex.digital_blog.domain.User;
import vn.ibex.digital_blog.domain.response.ResCreateUserDTO;
import vn.ibex.digital_blog.domain.response.ResUserDTO;
import vn.ibex.digital_blog.domain.response.ResultPaginationDTO;
import vn.ibex.digital_blog.service.UserService;
import vn.ibex.digital_blog.util.annotation.ApiMessage;
import vn.ibex.digital_blog.util.error.IdInvalidException;

// import com.turkraft.springfilter.boot.Filter;

// Save User
// Delete User
// Read User (pagination, searchText)
// Activate User
// Deactivate User


@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    //@ApiMessage("create a new user")
    public ResponseEntity<ResCreateUserDTO> saveUser(@Valid @RequestBody User userPostMan) throws IdInvalidException {
        boolean isEmailExist = userService.existByEmail(userPostMan.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException("Email:" + userPostMan.getEmail()+" is already taken");
        }
        String hashPassword = this.passwordEncoder.encode(userPostMan.getPassword());
        userPostMan.setPassword(hashPassword);
        User newUser = userService.handleSaveUser(userPostMan);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResUserDTO(newUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        User currentUser = this.userService.fetchUserById(id);
        userService.handleDeleteUser(id);
        if(currentUser == null) {
            return ResponseEntity.notFound().build();
        }
        this.userService.handleDeleteUser(id);

        return ResponseEntity.noContent().build();
    }

    // fetch user by id
    @GetMapping("/users/{id}")
    @ApiMessage("fetch a user by id")
    public ResponseEntity<ResCreateUserDTO> getUserById(@PathVariable("id") long id)
    throws IdInvalidException {
        User user = this.userService.fetchUserById(id);
        if(user == null) {
            throw new IdInvalidException("User id: " + id + " is not found");
        }
        return ResponseEntity.ok(this.userService.convertToResUserDTO(user));
    }

    // fetch all user
    //@Filter Specification<User> spec
    // thiếu filter
    @GetMapping("/users")
    @ApiMessage("fetch all user")
    public ResponseEntity<ResultPaginationDTO> getAllUser( Pageable pageable){
        ResultPaginationDTO resultPaginationDTO = this.userService.fetchAllUser( pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }


    @GetMapping("/helo")
    @ApiMessage("fetch all helo")
    public String hello( ){
        return "Hello";
    }

    @PutMapping("/users")
    @ApiMessage("update a user")
    public ResponseEntity<ResCreateUserDTO>  updateUser(@RequestBody User user)
    throws IdInvalidException{
        User currUser = this.userService.handleUpdateUser(user);
        if(currUser == null) {
            throw new IdInvalidException("User id: " + user.getId() + " is not found");
        }
        return ResponseEntity.ok(this.userService.convertToResUserDTO(currUser));
    }


    @PutMapping("/users/{id}")
    @ApiMessage("deactive a user")
    public ResponseEntity<ResCreateUserDTO>  deactivateUser(@RequestBody User user)
    throws IdInvalidException{
        User currUser = this.userService.handleUpdateUser(user);
        if(currUser == null) {
            throw new IdInvalidException("User id: " + user.getId() + " is not found");
        }
        return ResponseEntity.ok(this.userService.convertToResUserDTO(currUser));
    }

    

}
