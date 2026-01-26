package it.unicam.cs.diciotto.progettoids.service;

import org.springframework.stereotype.Service;

@Service
public class NotificaService implements INotificaService {
    @Override
    public void inviaNotifica(String destinatario, String messaggio) {
        // Placeholder: integrate email/SMS/push
    }
}
