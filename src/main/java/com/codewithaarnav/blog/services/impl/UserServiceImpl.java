package com.codewithaarnav.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codewithaarnav.blog.entities.Role;
import com.codewithaarnav.blog.entities.User;
import com.codewithaarnav.blog.exceptions.ResourceNotFoundException;
import com.codewithaarnav.blog.payloads.UserDto;
import com.codewithaarnav.blog.repositories.RoleRepo;
import com.codewithaarnav.blog.repositories.UserRepo;
import com.codewithaarnav.blog.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    // CREATE USER
    @Override
    public UserDto createUser(UserDto userDto) {

        User user = this.dtoToUser(userDto);

        // Encrypt password before saving
        user.setPassword(
                this.passwordEncoder.encode(userDto.getPassword())
        );

        // Assign ROLE_USER by default
        Role roleUser = this.roleRepo.findByName("ROLE_USER")
                .orElseThrow(() ->
                        new RuntimeException("ROLE_USER not found")
                );

        user.getRoles().add(roleUser);

        User savedUser = this.userRepo.save(user);

        return this.userToDto(savedUser);
    }

    // UPDATE USER
    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User", "id", userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        // Encrypt new password
        user.setPassword(
                this.passwordEncoder.encode(userDto.getPassword())
        );

        user.setAbout(userDto.getAbout());

        User updatedUser = this.userRepo.save(user);

        return this.userToDto(updatedUser);
    }

    // GET USER BY ID
    @Override
    public UserDto getUserById(Integer userId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User", "id", userId));

        return this.userToDto(user);
    }

    // GET ALL USERS
    @Override
    public List<UserDto> getAllUsers() {

        List<User> users = this.userRepo.findAll();

        List<UserDto> userDtos = users.stream()
                .map(user -> this.userToDto(user))
                .collect(Collectors.toList());

        return userDtos;
    }

    // DELETE USER
    @Override
    public void deleteUser(Integer userId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User", "id", userId));

        this.userRepo.delete(user);
    }

    // Convert UserDto to User
    public User dtoToUser(UserDto userDto) {

        User user = this.modelMapper.map(userDto, User.class);

        return user;
    }

    // Convert User to UserDto
    public UserDto userToDto(User user) {

        UserDto userDto =
                this.modelMapper.map(user, UserDto.class);

        // Never expose password/hash
        userDto.setPassword(null);

        return userDto;
    }
}