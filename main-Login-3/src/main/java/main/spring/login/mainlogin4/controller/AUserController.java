package main.spring.login.mainlogin4.controller;

import main.spring.login.mainlogin4.entity.UserEntity;
import main.spring.login.mainlogin4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/ausers")
public class AUserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/findallusername")
    public Map<String, Boolean> findUsername(@RequestBody Map<String, String> request) {
        Map<String, Boolean> response = new HashMap<>();
        String userInS = request.get("userNowS");
        // 클라이언트로부터 받은 code를 이용하여 username을 DB에서 찾음
        // 이 예시에서는 단순히 code를 username으로 사용함
        // 실제로는 해당하는 사용자를 DB에서 찾아야 함


            UserEntity userNowS = userRepository.findByUsername(userInS);
            response.put("usernameExists", userNowS != null);
            // username이 null이 아니면 존재하는 것으로 판단


        System.out.println(response);

        return response;
    }
}