package com.jacobIbrom.finance_tracker.repository;
import com.jacobIbrom.finance_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface  UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
