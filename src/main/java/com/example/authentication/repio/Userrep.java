package com.example.authentication.repio;

import com.example.authentication.entity.Userentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Userrep extends JpaRepository<Userentity,Long> {

    Optional<Userentity> findByEmail(String email);

}
