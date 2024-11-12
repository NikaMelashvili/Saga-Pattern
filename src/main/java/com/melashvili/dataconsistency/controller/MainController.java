package com.melashvili.dataconsistency.controller;

import com.melashvili.dataconsistency.model.request.AddUserRequestDTO;
import com.melashvili.dataconsistency.model.request.UpdateUserRequestDTO;
import com.melashvili.dataconsistency.services.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/pattern")
public class MainController {

    private UpdateService updateService;

    @Autowired
    public void setUpdateService(UpdateService updateService) {
        this.updateService = updateService;
    }

    @PostMapping("/save")
    public ResponseEntity<Void> addUser(@RequestBody AddUserRequestDTO addUserRequestDTO) {
        updateService.addUser(addUserRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<String> ACIDUpdate(@RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
        String response = updateService.ADICUpdate(updateUserRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
