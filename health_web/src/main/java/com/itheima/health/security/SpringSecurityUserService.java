package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.itheima.health.pojo.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Description：登录用户认证与授权
 * 记得要把这个类注册到spring容器
 * User：Eric
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (null != user){
            //用户名
            //密码
            String password = user.getPassword();
            //权限集合
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            //授权
            //用户所拥有的角色
            SimpleGrantedAuthority sai = null;
            Set<Role> roles = user.getRoles();
            if (null != roles){
                for (Role role : roles) {
                    //角色用关键字，授予角色
                    sai = new SimpleGrantedAuthority(role.getKeyword());
                    authorities.add(sai);
                    //权限，角色下的所有权限
                    Set<Permission> permissions = role.getPermissions();
                    if (null != permissions){
                        for (Permission permission : permissions) {
//                            授予权限
                            sai = new SimpleGrantedAuthority(permission.getKeyword());
                            authorities.add(sai);
                        }
                    }
                }
            }
            return new org.springframework.security.core.userdetails.User(username,password,authorities);
        }
        //返回null，限制访问
        return null;
    }
}
