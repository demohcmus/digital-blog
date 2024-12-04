package vn.ibex.digital_blog.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.ibex.digital_blog.domain.Permission;
import vn.ibex.digital_blog.domain.response.ResultPaginationDTO;

public interface PermissionService {
    boolean isPermissionExist(Permission permission);

    Permission fetchById(long id);

    Permission create(Permission permission);

    Permission update(Permission permission);

    void delete(long id);

    ResultPaginationDTO getPermissions(Specification<Permission> spec, Pageable pageable);
}
