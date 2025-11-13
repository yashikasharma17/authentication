package com.example.authentication.repio;

import com.example.authentication.entity.Userentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Userrep extends JpaRepository<Userentity,Long> {

    boolean findByEmail(String Email);
}
