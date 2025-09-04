package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<Message> createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank()) {
            return ResponseEntity.status(400).build();
        }
        if (message.getMessageText().length() > 255 || !accountRepository.existsById(message.getPostedBy())) {
            return ResponseEntity.status(400).build();
        }
        Message newMessage = messageRepository.save(message);
        return ResponseEntity.ok(newMessage);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public ResponseEntity<Message> getMessageById(int messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        return message.isPresent() ? ResponseEntity.ok(message.get()) : ResponseEntity.ok().build();
    }

    public ResponseEntity<Integer> deleteMessage(int messageId) {
        if (!messageRepository.existsById(messageId)) {
            return ResponseEntity.ok().build();
        }
        messageRepository.deleteById(messageId);
        return ResponseEntity.ok(1);
    }

    public ResponseEntity<Integer> updateMessage(int messageId, String updateText) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isEmpty() || updateText == null) {
            return ResponseEntity.status(400).build();
        }
        if (updateText.isBlank() || updateText.length() > 255) {
            return ResponseEntity.status(400).build();
        }
        Message message = optionalMessage.get();
        message.setMessageText(updateText);
        messageRepository.save(message);
        return ResponseEntity.ok(1);
    }

    public ResponseEntity<List<Message>> getMessagesByAccountId(int accountId) {
        List<Message> messages = messageRepository.findByPostedBy(accountId);
        return ResponseEntity.ok(messages);
    }   
}
