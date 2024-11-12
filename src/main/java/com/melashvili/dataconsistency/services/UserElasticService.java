package com.melashvili.dataconsistency.services;

import com.melashvili.dataconsistency.model.elasticModel.UserElastic;
import com.melashvili.dataconsistency.model.elasticModel.UserInfoElastic;
import com.melashvili.dataconsistency.model.entities.User;
import com.melashvili.dataconsistency.repositories.UserElasticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserElasticService {

    private UserElasticRepository userElasticRepository;

    @Autowired
    public void setUserElasticRepository(UserElasticRepository userElasticRepository) {
        this.userElasticRepository = userElasticRepository;
    }

    public void saveToElasticsearch(User user) {
        UserElastic userElastic = new UserElastic();
        userElastic.setId(user.getId());
        userElastic.setFirstName(user.getFirstName());
        userElastic.setLastName(user.getLastName());

        UserInfoElastic userInfoElastic = new UserInfoElastic();
        userInfoElastic.setId(user.getUserInfo().getId());
        userInfoElastic.setBirthDate(user.getUserInfo().getBirthDate());
        userInfoElastic.setAddress(user.getUserInfo().getAddress());

        userElastic.setUserInfo(userInfoElastic);

        userElasticRepository.save(userElastic);
    }
}
