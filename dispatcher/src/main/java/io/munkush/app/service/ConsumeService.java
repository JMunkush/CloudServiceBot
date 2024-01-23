package io.munkush.app.service;

import io.munkush.app.controller.UpdateController;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static io.munkush.app.RabbitQueue.ANSWER_MESSAGE;

@Service
@RequiredArgsConstructor
public class ConsumeService {
    private final UpdateController updateController;

    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consumeSendMessage(SendMessage sendMessage){
        updateController.sendMessage(sendMessage);
    }
}
