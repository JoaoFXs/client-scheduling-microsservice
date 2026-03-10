package br.com.joaofxs.client_scheduling_microsservice.core.service;


import br.com.joaofxs.client_scheduling_microsservice.core.exception.PasswordAlreadyUsedException;
import br.com.joaofxs.client_scheduling_microsservice.core.exception.TokenInvalidException;
import br.com.joaofxs.client_scheduling_microsservice.core.model.LastPassword;
import br.com.joaofxs.client_scheduling_microsservice.core.model.ResetToken;
import br.com.joaofxs.client_scheduling_microsservice.core.model.User;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.OldPasswordsCleanUpRepository;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.ResetTokenRepository;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.UserRepository;
import br.com.joaofxs.client_scheduling_microsservice.core.utils.GenerateResetToken;
import br.com.joaofxs.client_scheduling_microsservice.core.utils.NotificationComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordForgotService {

    private final UserRepository userRepository;
    private final GenerateResetToken generateResetToken;
    private final ResetTokenRepository resetTokenRepositoy;
    private final PasswordEncoder passwordEncoder;
    private final NotificationComponent notificationComponent;
    private final OldPasswordsCleanUpRepository passwordsCleanUpRepository;

    public void processPasswordReset(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()) return;

        User user = userOptional.get();
        String token = generateResetToken.makeResetToken();

        ResetToken resetToken = ResetToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
        resetTokenRepositoy.save(resetToken);

        notificationComponent.sendEmail(user.getEmail(), token);


    }

    public void validateToken(String token) {
        Optional<ResetToken> tokenReturn =  resetTokenRepositoy.findByToken(token);
        if(tokenReturn.isEmpty() || tokenReturn.get().isExpired()){
            throw new TokenInvalidException();
        }

    }

    public void resetPassword(String token, String newPassword) {
        Optional<ResetToken> tokenReturn =  resetTokenRepositoy.findByToken(token);
        if(tokenReturn.isEmpty() || tokenReturn.get().isExpired()){
            throw new TokenInvalidException();
        }


        User user = tokenReturn.get().getUser();

        List<LastPassword> lastPasswordList = passwordsCleanUpRepository.findAllByUser(user);

        for (LastPassword lastPassword : lastPasswordList){
            if(passwordEncoder.matches(newPassword, lastPassword.getPassword())){
                throw new PasswordAlreadyUsedException();
            }
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);


        passwordsCleanUpRepository.save(LastPassword
                .builder()
                .user(user)
                .password(passwordEncoder.encode(passwordEncoder.encode(newPassword)))
                .build());

        resetTokenRepositoy.delete(tokenReturn.get());
    }


}
