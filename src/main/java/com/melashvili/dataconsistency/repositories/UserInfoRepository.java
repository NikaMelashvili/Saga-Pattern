package com.melashvili.dataconsistency.repositories;

import com.melashvili.dataconsistency.model.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

    @Query("select i from UserInfo i where id = :id")
    UserInfo findInfoById(@Param("id")Integer id);
}
