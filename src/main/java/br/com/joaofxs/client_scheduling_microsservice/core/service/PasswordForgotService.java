package br.com.joaofxs.client_scheduling_microsservice.core.service;


import br.com.joaofxs.client_scheduling_microsservice.core.exception.TokenInvalidException;
import br.com.joaofxs.client_scheduling_microsservice.core.model.ResetToken;
import br.com.joaofxs.client_scheduling_microsservice.core.model.User;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.ResetTokenRepository;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.UserRepository;
import br.com.joaofxs.client_scheduling_microsservice.core.utils.GenerateResetToken;
import br.com.joaofxs.client_scheduling_microsservice.core.utils.NotificationComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordForgotService {


    private final UserRepository userRepository;

    private final GenerateResetToken generateResetToken;

    private final ResetTokenRepository resetTokenRepositoy;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationComponent notificationComponent;

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

    public boolean validateToken(String token) {
        Optional<ResetToken> tokenReturn =  resetTokenRepositoy.findByToken(token);
        if(tokenReturn.isEmpty() || tokenReturn.get().isExpired()){
            throw new TokenInvalidException();
        }

        return true;
    }

    public void resetPassword(String token, String newPassword) {
        Optional<ResetToken> tokenReturn =  resetTokenRepositoy.findByToken(token);
        if(tokenReturn.isEmpty() || tokenReturn.get().isExpired()){
            throw new TokenInvalidException();
        }
        User user = tokenReturn.get().getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetTokenRepositoy.delete(tokenReturn.get());
    }


}
