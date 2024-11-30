package vn.ibex.digital_blog.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

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

}
