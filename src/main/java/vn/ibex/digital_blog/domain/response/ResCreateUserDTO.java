package vn.ibex.digital_blog.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ibex.digital_blog.domain.Role;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String email;
    private String phone;
    private String address;
    private String fullname;
    private Instant dob;
    private boolean active;
    private RoleUser role;
     @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUser {
        private long id;
        private String name;
    }

}
