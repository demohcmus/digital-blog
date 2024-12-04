package vn.ibex.digital_blog.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ibex.digital_blog.domain.Permission;
import vn.ibex.digital_blog.domain.Role;
import vn.ibex.digital_blog.domain.response.ResultPaginationDTO;
import vn.ibex.digital_blog.repository.PermissionRepository;
import vn.ibex.digital_blog.repository.RoleRepository;
import vn.ibex.digital_blog.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    
    public RoleServiceImpl(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }
    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);
    }
    public Role create(Role r) {
        // check permissions
        if (r.getPermissions() != null) {
            List<Long> reqPermissions = r.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            r.setPermissions(dbPermissions);
        }
        return this.roleRepository.save(r);
    }
    public Role fetchById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent())
            return roleOptional.get();
        return null;
    }


    public Role update(Role r) {
        Role roleDB = this.fetchById(r.getId());
        // check permissions
        if (r.getPermissions() != null) {
            List<Long> reqPermissions = r.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            r.setPermissions(dbPermissions);
        }
        roleDB.setName(r.getName());
        roleDB.setPermissions(r.getPermissions());
        roleDB = this.roleRepository.save(roleDB);
        return roleDB;
    }
    public void delete(long id) {
        this.roleRepository.deleteById(id);
    }
    public ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> pRole = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pRole.getTotalPages());
        mt.setTotal(pRole.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pRole.getContent());
        return rs;
    }
}
