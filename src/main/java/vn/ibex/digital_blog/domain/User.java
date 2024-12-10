package vn.ibex.digital_blog.domain;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import vn.ibex.digital_blog.util.annotation.StrongPassword;

@Table(name= "users")
@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @NotBlank(message = "Email is required")
    @Email(message= "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=6, message = "Password should be at least 6 characters")
    //@StrongPassword
    private String password;



    private String phone;
    private String address;
    private String firstName;
    private String lastName;
    private Instant dob;
    
    private boolean active;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Article> articles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> comments;



    @Column(columnDefinition="text")
    private String refreshToken;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    
    

}
