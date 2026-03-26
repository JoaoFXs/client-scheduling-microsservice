package br.com.joaofxs.client_scheduling_microsservice.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos extras que o Google possa enviar no futuro
public class SocialLoginUserDTO {

    private String iss;
    private String azp;
    private String aud;
    private String sub;
    private String email;

    @JsonProperty("email_verified")
    private boolean emailVerified;

    private Long nbf;
    private String name;
    private String picture;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    private Long iat;
    private Long exp;
    private String jti;
}