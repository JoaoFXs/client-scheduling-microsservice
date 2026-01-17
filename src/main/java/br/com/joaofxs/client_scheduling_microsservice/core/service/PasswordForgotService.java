package br.com.joaofxs.client_scheduling_microsservice.core.service;


import br.com.joaofxs.client_scheduling_microsservice.core.exception.TokenInvalidException;
import br.com.joaofxs.client_scheduling_microsservice.core.model.ResetToken;
import br.com.joaofxs.client_scheduling_microsservice.core.model.User;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.ResetTokenRepository;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.UserRepository;
import br.com.joaofxs.client_scheduling_microsservice.core.utils.GenerateResetToken;
import br.com.joaofxs.client_scheduling_microsservice.core.utils.NotificationComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordForgotService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenerateResetToken generateResetToken;

    @Autowired
    private ResetTokenRepository resetTokenRepositoy;

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

    public void validateToken(String token) {
        Optional<ResetToken> tokenReturn =  resetTokenRepositoy.findByToken(token);

        if(tokenReturn.isEmpty() || tokenReturn.get().isExpired()){
            throw new TokenInvalidException();
        }

    }
}
