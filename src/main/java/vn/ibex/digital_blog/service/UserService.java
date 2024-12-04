package vn.ibex.digital_blog.service;

import org.springframework.data.domain.Pageable;

import vn.ibex.digital_blog.domain.User;
import vn.ibex.digital_blog.domain.response.ResCreateUserDTO;
import vn.ibex.digital_blog.domain.response.ResultPaginationDTO;

public interface UserService {
    User handleSaveUser(User user);

    boolean existByEmail(String email);

    User fetchUserById(long id);

    void handleDeleteUser(long id);

    ResCreateUserDTO convertToResUserDTO(User user);

    ResultPaginationDTO fetchAllUser(String searchText, Pageable pageable);

    User handleUpdateUser(User reqUser, String email);

    User handleGetUserByUsername(String email);

    boolean isEmailExist(String email);

    void updateUserToken(String token, String email);

    User getUserByRefreshTokenAndEmail(String token, String email);

    User deactiveOrActiveUser(User user);
}