package com.melashvili.dataconsistency.services;

import com.melashvili.dataconsistency.model.entities.UserInfo;
import com.melashvili.dataconsistency.repositories.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    private UserInfoRepository userInfoRepository;

    @Autowired
    public void setUserInfoRepository(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public void save(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }
}
