package com.example.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/ignore")
@Slf4j
public class IgnoreController {

    @Autowired
    private CloudUserMapper cloudUserMapper;

    @Autowired
    private FtpOperation ftpOperation;

    @GetMapping(value = "/getUser")
    public void getUser(@RequestParam("id") String id) throws IOException {
         InputStream inputStream = ftpOperation.downloadFile("1.txt");
         ftpOperation.copyFile(inputStream);
//        CloudUser cloudUser = cloudUserMapper.getById();
    }
}
