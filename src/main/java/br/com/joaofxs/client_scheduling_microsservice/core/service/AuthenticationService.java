package br.com.joaofxs.client_scheduling_microsservice.core.service;

import br.com.joaofxs.client_scheduling_microsservice.admin.dto.SocialLoginUserDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.AccessToken;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.SocialLoginRequest;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.UserDTO;
import br.com.joaofxs.client_scheduling_microsservice.core.exception.UserAlreadyExistException;
import br.com.joaofxs.client_scheduling_microsservice.core.model.LastPassword;
import br.com.joaofxs.client_scheduling_microsservice.core.model.User;
import br.com.joaofxs.client_scheduling_microsservice.core.dto.AuthRequest;
import br.com.joaofxs.client_scheduling_microsservice.core.model.enums.TypeUser;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.OldPasswordsCleanUpRepository;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.text.WordUtils;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final OldPasswordsCleanUpRepository passwordsCleanUpRepository;

    public AccessToken register(UserDTO userDTO, String role) {
        User user = User.builder()
                    .username(WordUtils.capitalizeFully(userDTO.username()))
                    .email(userDTO.email())
                    .phone(userDTO.phone())
                    .cpf(userDTO.cpf())
                    .build();

        // Verifica se o usuário já existe
        if(userRepository.getByEmail(userDTO.email()) != null){
            throw new UserAlreadyExistException("Email já cadastrado");
        }

        Set<TypeUser> roles = determineRoles(role, userDTO.isCompany());
        user.setRoles(roles);

        System.out.println(user.getAuthorities());
        // Criptografa a senha antes de salvar
        user.setPassword(passwordEncoder.encode(userDTO.password()));

        repository.save(user);

        passwordsCleanUpRepository.save(LastPassword
                        .builder()
                        .user(user)
                        .password(passwordEncoder.encode(userDTO.password()))
                        .build());

        return jwtService.generateToken(user);
    }


    public AccessToken authenticate(AuthRequest request) {
        var user = userRepository.getByEmail(request.email());

        if(user == null){
            throw new UsernameNotFoundException("Usuário ou senha incorretos");
        }

        if (user.getSub() != null){
            return jwtService.generateToken(user);
        }
        if(passwordEncoder.matches(request.password(), user.getPassword())){
            return jwtService.generateToken(user);
        }
        throw new UsernameNotFoundException("Usuário ou senha incorretos");
    }

    public void socialLoginRequest(String jwtToken, AuthRequest authRequest){
        SocialLoginRequest socialLoginRequest = buildSocialLoginRequest(jwtToken);
        var user = userRepository.findBySub(socialLoginRequest.getSub());

        if (user.isPresent()){
            throw new UserAlreadyExistException("Usuario já cadastrado");
        }
        User newUser = User
                        .builder()
                        .username(authRequest.username())
                        .email(socialLoginRequest.getEmail())
                        .roles(socialLoginRequest.getRole())
                        .phone(authRequest.phone())
                        .cpf(authRequest.cpf())
                        .sub(socialLoginRequest.getSub())
                        .provider(socialLoginRequest.getProvider())
                        .password(passwordEncoder.encode(authRequest.password()))
                        .build();

        userRepository.save(newUser);
    }



    public AccessToken verifyIfUserExist(String jwtToken){
        SocialLoginRequest socialLoginRequest = buildSocialLoginRequest(jwtToken);
        var user = userRepository.findBySub(socialLoginRequest.getSub());
        return jwtService.generateToken(user.get());
    }


    public SocialLoginRequest buildSocialLoginRequest(String jwtToken){
        SocialLoginUserDTO payload = jwtService.decodeBasicJwt(jwtToken);

        return SocialLoginRequest.builder()
                .sub(payload.getSub())
                .name(payload.getName())
                .email(payload.getEmail())
                .role(Set.of(TypeUser.USER, TypeUser.CUSTUMER))
                .provider(payload.getProvider())
                .build();
    }

    // --- Método Auxiliar de register ---
    private Set<TypeUser> determineRoles(String role, boolean isAdmin) {
        if (!role.contains("user")) {
            return Set.of(TypeUser.USER, TypeUser.ADMIN);
        }

        // Usando a lógica ternária para decidir entre ENTERPRISE e CUSTOMER
        TypeUser specificType = isAdmin ? TypeUser.ENTERPRISE : TypeUser.CUSTUMER;
        return Set.of(TypeUser.USER, specificType);
    }


}