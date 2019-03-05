package com.mms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mms.models.CountersModel;

@RestController("APIController")
public class APIController {

    @Autowired
    CountersModel countModel;

    @GetMapping("/Types")
    public String countTypes() {
        return countModel.printTypes();
    }

    @GetMapping("/LogInfo")
    public String countWords() {
        return countModel.printWords();
    }
}
