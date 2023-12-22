package csie.bdsa.lms.shared.client;

import csie.bdsa.lms.shared.dto.UserDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", contextId = "authFeignClient")
public interface AuthFeignClient {

    @GetMapping("/users/username/{username}")
    UserDetailsDto getUser(@PathVariable String username);
}
