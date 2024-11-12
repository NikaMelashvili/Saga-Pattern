package com.melashvili.dataconsistency.services;

import com.melashvili.dataconsistency.model.entities.User;
import com.melashvili.dataconsistency.model.entities.UserInfo;
import com.melashvili.dataconsistency.model.request.AddUserRequestDTO;
import com.melashvili.dataconsistency.model.request.UpdateUserRequestDTO;
import com.melashvili.dataconsistency.repositories.UserInfoRepository;
import com.melashvili.dataconsistency.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateService {

    private UserService userService;

    private UserInfoService userInfoService;

    private UserElasticService userElasticService;

    private UserRepository userRepository;

    private UserInfoRepository userInfoRepository;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Autowired
    public void setUserElasticService(UserElasticService userElasticService) {
        this.userElasticService = userElasticService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserInfoRepository(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public void addUser(AddUserRequestDTO addUserRequestDTO) {
        UserInfo userInfo = new UserInfo();
        userInfo.setBirthDate(addUserRequestDTO.getBirthDate());
        constructUser(userInfo, addUserRequestDTO.getAddress(), addUserRequestDTO.getFirstName(), addUserRequestDTO.getLastName());
    }

    @Transactional
    public String ADICUpdate(UpdateUserRequestDTO updateUserRequestDTO) {
        String sagaId = generateSagaId();

        try {
            UserInfo newUserInfo = new UserInfo();
            newUserInfo.setAddress(updateUserRequestDTO.getAddress());

            User newUser = new User();
            newUser.setFirstName(updateUserRequestDTO.getFirstName());
            newUser.setLastName(updateUserRequestDTO.getLastName());
            newUser.setUserInfo(newUserInfo);

            User oldUser;
            try {
                oldUser = userRepository.findById(updateUserRequestDTO.getOldId());
            } catch (Exception e) {
                throw new RuntimeException("User with old ID not found");
            }
            Integer oldId = oldUser.getId();

            userInfoRepository.deleteById(oldId);
            userRepository.deleteById(oldId);

            newUserInfo.setId(null);
            userInfoService.save(newUserInfo);
            Integer newId = newUserInfo.getId();

            newUser.setId(newId);
            userService.save(newUser);

            userElasticService.saveToElasticsearch(newUser);

            return "User with old ID " + updateUserRequestDTO.getOldId() + " was successfully updated to new ID " + newUserInfo.getId();

        } catch (Exception e) {
            rollbackUpdate(updateUserRequestDTO.getOldId(), sagaId);
            return "Update failed, compensating actions executed.";
        }
    }

    // Compensation logic (Rollback)
    private void rollbackUpdate(int oldId, String sagaId) {
        try {
            try {
                User oldUser = userRepository.findById(oldId);
            } catch (Exception e) {
                throw new RuntimeException("User with old ID not found");
            }

            userElasticService.rollback(sagaId);

            // Rollback new User and UserInfo entities
            userRepository.deleteById(oldUser.getId());
            userInfoRepository.deleteById(oldUser.getUserInfo().getId());

        } catch (Exception rollbackException) {
            // Log rollback exception if needed
        }
    }

    private String generateSagaId() {
        return UUID.randomUUID().toString();
    }

    private void constructUser(UserInfo updatedUserInfo,
                               String address,
                               String firstName,
                               String lastName) {
        updatedUserInfo.setAddress(address);

        userInfoService.save(updatedUserInfo);

        User updatedUser = new User();
        updatedUser.setId(updatedUserInfo.getId());
        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setUserInfo(updatedUserInfo);

        userService.save(updatedUser);

        userElasticService.saveToElasticsearch(updatedUser);
    }

    private boolean deleteUser(int oldId) {
        try {
            userInfoRepository.deleteById(oldId);
            userRepository.deleteById(oldId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
