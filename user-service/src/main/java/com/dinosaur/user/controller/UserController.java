package com.dinosaur.user.controller;

import com.dinosaur.user.vo.UserVO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.ConnectException;

@RestController
public class UserController {

    private int i=0;
    @GetMapping(path = "/hello/{name}")
    public Mono<String> getName(@PathVariable String name) {
        return Mono.just(name);
    }

    @GetMapping("/getUsers")
    public Flux<UserVO> getUsers(@RequestParam String userId, @RequestParam String userName) {
        UserVO u1 = new UserVO();
        u1.setUserId("1");
        u1.setUserName("lucy");
        UserVO u2 = new UserVO();
        u2.setUserId("2");
        u2.setUserName("jack");
        System.out.println(u1);
        return Flux.just(u1,u2);
    }

    @PostMapping("/user/save")
    public Mono<UserVO> save(@RequestBody UserVO userVO) {
//        System.out.println(userVO.getUserName());
        return Mono.just(userVO);
    }

    @GetMapping("/retry_test")
    public Mono<String> retryTest() {
        System.out.println("retry第"+i+"次");
        i++;
        int a=1;
        if(i<3) {
            int b = a / 0;
        }
        return Mono.just("retry第"+i+"次");
    }
}
