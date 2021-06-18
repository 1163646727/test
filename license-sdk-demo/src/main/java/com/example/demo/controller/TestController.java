package com.example.demo.controller;

import com.bocloud.paas.license.sdk.annotation.LicenseCheck;
import com.bocloud.paas.license.sdk.annotation.LicenseParam;
import com.example.demo.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class TestController {

    @RequestMapping("/test1")
    @LicenseCheck(field = "nodes", edition = {"prod"}, checker = "test", params = "#request.getParameter('nodes')")
    public String test(@LicenseParam HttpServletRequest request) {
        return "ok";
    }

    @RequestMapping("/test16")
    @LicenseCheck()
    public String test6() {
        return "ok";
    }

    @RequestMapping("/test2")
    @LicenseCheck(field = "nodes", edition = {"prod"}, checker = "test", params = "#nodes")
    public String test(@LicenseParam int nodes) {
        return "ok";
    }

    @RequestMapping("/test3")
    @LicenseCheck(field = "nodes", edition = {"prod"}, checker = "test", params = "#user.nodes")
    public String test(@LicenseParam User user) {
        return "ok";
    }

    @RequestMapping("/test4")
    @LicenseCheck(field = "nodes", edition = {"prod"}, checker = "test", params = "#user.nodes")
    public String test4(@LicenseParam @RequestBody User user) {
        return "ok";
    }

    @RequestMapping("/test5")
    @LicenseCheck(field = "nodes", edition = {"prod"}, checker = "test", params = "#user.nodes")
    public String test5(String name, String psw, @LicenseParam User user) {
        System.out.println(name);
        System.out.println(psw);
        return "ok";
    }

}
