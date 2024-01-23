package io.munkush.app.service;

import io.munkush.app.entity.User;
import io.munkush.app.repository.UserRepository;
import io.munkush.app.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;

import static io.munkush.app.RabbitQueue.ANSWER_MESSAGE;
import static io.munkush.app.util.MessageUtil.GREETING;

@Service
@RequiredArgsConstructor
@Slf4j
public class TextService {
    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;
    public void produce(Update update) {

        var text = update.getMessage().getText();


        if (text.equals("/start")) {
            sendGreetingMessage(GREETING, update.getMessage().getChatId());
            saveUser(update);
        }

    }


    private void saveUser(Update update) {
        var from = update.getMessage().getFrom();

        var user = User.builder()
                .tgId(from.getId())
                .firstName(from.getFirstName())
                .username(from.getUserName())
                .build();

        userRepository.save(user);
    }

    private void sendGreetingMessage(String output, Long chatId){

        SendMessage sendMessage = new SendMessage();

        sendMessage.setReplyMarkup(MessageUtil.getKeyboardByPattern(1,2, List.of("Отправить файл",
                        "Смотреть файлы"),
                List.of("send", "read")));

        sendMessage.setText(output);
        sendMessage.setChatId(chatId);

        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }
}
