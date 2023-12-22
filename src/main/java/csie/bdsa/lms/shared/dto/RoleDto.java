package csie.bdsa.lms.shared.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RoleDto extends BaseDto<Long> implements GrantedAuthority {

    @NotBlank(message = "Authority is mandatory")
    private String authority;

}
