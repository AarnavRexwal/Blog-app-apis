package com.codewithaarnav.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithaarnav.blog.payloads.ApiResponse;
import com.codewithaarnav.blog.payloads.UserDto;
import com.codewithaarnav.blog.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // POST - Create User
    @PostMapping("/")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {

        UserDto createdUserDto = this.userService.createUser(userDto);

        return new ResponseEntity<>(
                createdUserDto,
                HttpStatus.CREATED
        );
    }

    // PUT - Update User
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#uId, authentication.name)")
    public ResponseEntity<UserDto> updateUser(
            @RequestBody UserDto userDto,
            @PathVariable("userId") Integer uId) {

        UserDto updatedUser =
                this.userService.updateUser(userDto, uId);

        return ResponseEntity.ok(updatedUser);
    }
    
    

    // DELETE - Delete User
    
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#uid, authentication.name)")
    public ResponseEntity<ApiResponse> deleteUser(
            @PathVariable("userId") Integer uid) {

        this.userService.deleteUser(uid);

        return new ResponseEntity<ApiResponse>(
                new ApiResponse(
                        "User Deleted Successfully",
                        true
                ),
                HttpStatus.OK
        );
    }
    
 // GET - Get All User
    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers(){
    	return ResponseEntity.ok(this.userService.getAllUsers());
    }

    // GET - Get Single User
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#userId, authentication.name)")
    public ResponseEntity<UserDto> getSingleUser(
            @PathVariable("userId") Integer userId) {

        return ResponseEntity.ok(
                this.userService.getUserById(userId)
        );
    }
    
}