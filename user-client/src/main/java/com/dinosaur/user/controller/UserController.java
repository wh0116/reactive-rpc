package com.dinosaur.user.controller;

import com.dinosaur.user.rpc.UserRpc;
import com.dinosaur.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
public class UserController {

    @Autowired
    private UserRpc userRpc;

    @GetMapping(path = "/hello/{name}")
    public Mono<String> getName(@PathVariable String name) {
        return userRpc.getName(name);
    }

    @PostMapping("/user/save")
    public Flux<UserVO> save(@RequestBody UserVO userVO) {
        long start = System.currentTimeMillis();
        return Flux.range(1,100000).flatMap(f-> userRpc.save(userVO))
                .doFinally(f->System.out.println("time cost:"+(System.currentTimeMillis()-start)));

    }

    @GetMapping("/getUsers")
    public Flux<UserVO> getUsers(@RequestParam String userId, @RequestParam String userName) {
        return userRpc.getUsers(userId,userName);
    }

    @GetMapping("/retry_test")
    public Mono<String> retryTest(){
        return userRpc.retryTest().doOnError(e->System.out.println(e.getMessage()));
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<UserVO>> streamAllUsers() {
        List<UserVO> list = new ArrayList<>(2);
        UserVO u = new UserVO();
        u.setUserId("1");
        u.setUserName("jack");
        list.add(u);
        u.setUserId("2");
        u.setUserName("lucy");
        list.add(u);

        return Flux.just(list)
                .flatMap(user -> Flux.zip(Flux.interval(Duration.ofSeconds(2)),
                                        Flux.fromStream(Stream.generate(() -> user))
                                )
                                .map(Tuple2::getT2)
                );
    }
}
