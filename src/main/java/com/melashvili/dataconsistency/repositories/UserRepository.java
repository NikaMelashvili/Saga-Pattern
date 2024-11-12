package com.melashvili.dataconsistency.repositories;

import com.melashvili.dataconsistency.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.id = :id")
    User findById(@Param("id") int id);
}
