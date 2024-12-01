package vn.ibex.digital_blog.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ibex.digital_blog.domain.User;
import vn.ibex.digital_blog.domain.response.ResCreateUserDTO;
import vn.ibex.digital_blog.domain.response.ResUserDTO;
import vn.ibex.digital_blog.domain.response.ResultPaginationDTO;
import vn.ibex.digital_blog.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleSaveUser(User user) {
        return this.userRepository.save(user);
    }

    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User fetchUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public ResCreateUserDTO convertToResUserDTO(User user) {
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();
        ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();

        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
        }
        resCreateUserDTO.setId(user.getId());
        resCreateUserDTO.setEmail(user.getEmail());
        resCreateUserDTO.setPhone(user.getPhone());
        resCreateUserDTO.setAddress(user.getAddress());
        resCreateUserDTO.setFullname(String.format("%s %s", user.getFirstName(), user.getLastName()));
        resCreateUserDTO.setDob(user.getDob());
        resCreateUserDTO.setActive(user.isActive());
        return resCreateUserDTO;
    }

    public ResultPaginationDTO fetchAllUser(Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        List<ResCreateUserDTO> listUser = pageUser.getContent()
                .stream()
                .map(user -> this.convertToResUserDTO(user))
                .collect(Collectors.toList());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(listUser);

        return resultPaginationDTO;

    }

    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            if(reqUser.getPhone()!=null)
            currentUser.setPhone(reqUser.getPhone());
            if(reqUser.getAddress()!=null)
            currentUser.setAddress(reqUser.getAddress());
            if(reqUser.getFirstName()!=null)
            currentUser.setFirstName(reqUser.getFirstName());
            if(reqUser.getLastName()!=null)
            currentUser.setLastName(reqUser.getLastName());
            if(reqUser.getDob()!=null)
            currentUser.setDob(reqUser.getDob());

            return this.userRepository.save(currentUser);
        }

        return null;
    }

    public User handleGetUserByUsername(String email) {
        return this.userRepository.findByEmail(email);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public void updateUserToken(String token, String email) {
        User currUser = this.handleGetUserByUsername(email);
        if (currUser != null) {
            currUser.setRefreshToken(token);
            this.userRepository.save(currUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

    public User deactiveOrActiveUser(User user) {
        user.setActive(!user.isActive());
        return this.userRepository.save(user);
    }

}
