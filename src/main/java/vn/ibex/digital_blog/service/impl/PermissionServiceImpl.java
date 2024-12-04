package vn.ibex.digital_blog.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ibex.digital_blog.domain.Permission;
import vn.ibex.digital_blog.domain.response.ResultPaginationDTO;
import vn.ibex.digital_blog.repository.PermissionRepository;
import vn.ibex.digital_blog.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
    public boolean isPermissionExist(Permission p) {
        return permissionRepository.existsByApiPathAndMethod(
                p.getApiPath(),
                p.getMethod());
    }
    
    public Permission fetchById(long id) {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        if (permissionOptional.isPresent())
            return permissionOptional.get();
        return null;
    }
    public Permission create(Permission p) {
        return this.permissionRepository.save(p);
    }
    public Permission update(Permission p) {
        Permission permissionDB = this.fetchById(p.getId());
        if (permissionDB != null) {
            permissionDB.setApiPath(p.getApiPath());
            permissionDB.setMethod(p.getMethod());
            // update
            permissionDB = this.permissionRepository.save(permissionDB);
            return permissionDB;
        }
        return null;
    }
    public void delete(long id) {
        // delete permission_role
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));
        // delete permission
        this.permissionRepository.delete(currentPermission);
    }
    public ResultPaginationDTO getPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pPermissions = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pPermissions.getTotalPages());
        mt.setTotal(pPermissions.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pPermissions.getContent());
        return rs;
    }
}