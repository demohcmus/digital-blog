package vn.ibex.digital_blog.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.ibex.digital_blog.domain.Role;
import vn.ibex.digital_blog.domain.response.ResultPaginationDTO;

public interface RoleService {

    boolean existByName(String name);

    Role create(Role role);

    Role fetchById(long id);

    Role update(Role role);

    void delete(long id);

    ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable);
}
