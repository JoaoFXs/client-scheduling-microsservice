package br.com.joaofxs.client_scheduling_microsservice.core.dto;

import br.com.joaofxs.client_scheduling_microsservice.core.model.enums.TypeUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class SocialLoginRequest{
    String name;
    String email;
    String sub;
    Set<TypeUser> role;
    String provider;


}