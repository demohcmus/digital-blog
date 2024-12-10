package vn.ibex.digital_blog.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import vn.ibex.digital_blog.domain.Permission;
import vn.ibex.digital_blog.domain.Role;
import vn.ibex.digital_blog.domain.User;
import vn.ibex.digital_blog.service.UserService;
import vn.ibex.digital_blog.util.SecurityUtil;
import vn.ibex.digital_blog.util.error.IdInvalidException;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        try {
            String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            String requestURI = request.getRequestURI();
            String httpMethod = request.getMethod();
            System.out.println(">>> RUN preHandle");
            System.out.println(">>> path= " + path);
            System.out.println(">>> httpMethod= " + httpMethod);
            System.out.println(">>> requestURI= " + requestURI);

            // check permission
            if (path.matches("/api/articles/\\d+/comments")) {
                return true; // Allow access without checking
            }

            String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                    ? SecurityUtil.getCurrentUserLogin().get()
                    : "";
            if (email != null && !email.isEmpty()) {
                User user = this.userService.handleGetUserByUsername(email);
                if (user != null) {
                    Role role = user.getRole();
                    if (role != null) {
                        List<Permission> permissions = role.getPermissions();
                        boolean isAllow = permissions.stream().anyMatch(item -> item.getApiPath().equals(path)
                                && item.getMethod().equals(httpMethod));

                        if (isAllow == false) {
                            throw new IdInvalidException("You don't have permission to access this endpoint.");
                        }
                    } else {
                        throw new IdInvalidException("You don't have permission to access this endpoint.");
                    }
                }
            }

            return true;
        } catch (IdInvalidException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(e.getMessage());
            return false;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An unexpected error occurred.");
            return false;
        }
    }
}
