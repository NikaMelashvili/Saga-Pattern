package com.melashvili.dataconsistency.services;

import com.melashvili.dataconsistency.model.entities.User;
import com.melashvili.dataconsistency.model.entities.UserInfo;
import com.melashvili.dataconsistency.model.request.AddUserRequestDTO;
import com.melashvili.dataconsistency.model.request.UpdateUserRequestDTO;
import com.melashvili.dataconsistency.repositories.UserInfoRepository;
import com.melashvili.dataconsistency.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void addUser(AddUserRequestDTO addUserRequestDTO) {
        UserInfo userInfo = new UserInfo();
        userInfo.setBirthDate(addUserRequestDTO.getBirthDate());
        constructUser(userInfo,
                addUserRequestDTO.getAddress(),
                addUserRequestDTO.getFirstName(),
                addUserRequestDTO.getLastName());
    }

    public String ADICUpdate(UpdateUserRequestDTO updateUserRequestDTO) {
        User oldUser = userRepository.findById(updateUserRequestDTO.getOldId());
        User newUser = null;

        try {
            UserInfo newUserInfo = createNewUserInfo(updateUserRequestDTO, oldUser);
            newUser = createNewUser(updateUserRequestDTO, newUserInfo);

            userElasticService.saveToElasticsearch(newUser);

            deleteOldUser(oldUser);

            return "User with old ID " + oldUser.getId() +
                    " was successfully updated with new ID " + newUser.getId();
        } catch (Exception e) {
            rollbackSaga(newUser.getId());
            return "Update failed, compensating actions executed.";
        }
    }

    private UserInfo createNewUserInfo(UpdateUserRequestDTO dto, User oldUser) {
        UserInfo userInfo = new UserInfo();
        userInfo.setBirthDate(oldUser.getUserInfo().getBirthDate());
        userInfo.setAddress(dto.getAddress());
        userInfoService.save(userInfo);
        return userInfo;
    }

    private User createNewUser(UpdateUserRequestDTO dto, UserInfo userInfo) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUserInfo(userInfo);
        userService.save(user);
        return user;
    }

    private void deleteOldUser(User oldUser) {
        if (oldUser != null) {
            userInfoRepository.deleteById(oldUser.getUserInfo().getId());
            userRepository.deleteById(oldUser.getId());
        }
    }

    private void rollbackSaga(Integer id) {
        rollbackNewUserInfo(id);
        rollbackNewUser(id);
    }

    private void rollbackNewUserInfo(Integer id) {
        UserInfo newUserInfo = userInfoRepository.findInfoById(id);
        if (newUserInfo != null) {
            userInfoRepository.delete(newUserInfo);
        }
    }

    private void rollbackNewUser(int id) {
        User newUser = userRepository.findById(id);
        if (newUser != null) {
            userRepository.delete(newUser);
        }
    }
}
