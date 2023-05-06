package com.dinosaur.user.rpc;

import com.dinosaur.reactive.webclient.core.annotation.HRpc;
import com.dinosaur.user.vo.UserVO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author wanghu
 */
@HRpc(value = "user-service", url = "http://localhost:8080")
public interface UserRpc {

    @GetMapping(path = "/hello/{name}")
    Mono<String> getName(@PathVariable String name);

    @GetMapping("/getUsers")
    Flux<UserVO> getUsers(@RequestParam String userId, @RequestParam String userName);

    @PostMapping("/user/save")
    Mono<UserVO> save(@RequestBody UserVO userVO);

    @GetMapping("/retry_test")
    Mono<String> retryTest();
}
