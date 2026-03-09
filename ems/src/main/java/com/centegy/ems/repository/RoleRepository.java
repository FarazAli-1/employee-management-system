package com.centegy.ems.repository;


import com.centegy.ems.model.Role;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class RoleRepository {

    private final List<Role> roleList = new ArrayList<>();

    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public Role save(Role role) {
        role.setId(idGenerator.getAndIncrement());
        roleList.add(role);
        return role;
    }

    public List<Role> findAll() {
        return roleList;
    }


}
