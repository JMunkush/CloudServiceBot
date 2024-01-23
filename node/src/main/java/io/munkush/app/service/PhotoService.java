package io.munkush.app.service;

import io.munkush.app.entity.State;
import io.munkush.app.repository.ContentRepository;
import io.munkush.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;

import static io.munkush.app.RabbitQueue.ANSWER_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {
    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    public void produce(Update update){
        var user = userRepository.findByTgId(update.getMessage().getFrom().getId())
                .orElseThrow();
        if(user.getState().equals(State.SENDING)){
            var photo = update.getMessage().getPhoto().get(0);

            var file = restTemplate.getForEntity(String.format("http://dispatcher:8080/getPhoto/%s", photo.getFileId()), File.class).getBody();

            if(file != null) {

                //TODO save file

                user.setState(State.AFK);
                userRepository.save(user);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(update.getMessage().getChatId());
                sendMessage.setText("Успешно добавлено");
                rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
            } else {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(update.getMessage().getChatId());
                sendMessage.setText("Не удалось сохранить данное фото");
            }
        }
    }

}
