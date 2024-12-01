package vn.ibex.digital_blog.domain;

import java.util.List;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ibex.digital_blog.constant.RoleEnum;

@Entity
@Table(name= "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Role name is required")
    @Enumerated(EnumType.STRING) // Lưu Enum dưới dạng chuỗi trong database
    private RoleEnum name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="roles")
    @JoinTable(
        name = "role_permission", 
        joinColumns = @JoinColumn(name = "role_id"), 
        inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions;
    
    // @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    // @JsonIgnore
    // List<User> users;
}
