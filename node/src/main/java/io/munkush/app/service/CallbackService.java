package io.munkush.app.service;

import io.munkush.app.entity.State;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class CallbackService {
    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;

    public void produce(Update update) {

        var data = update.getCallbackQuery().getData();

        if(data.equals("send")){
            doSendProcess(update);
        }
        else if(data.equals("cancel")){
            doCancelProcess(update);
        } else if(data.equals("read")){
            //TODO
        }

    }

    private void doCancelProcess(Update update) {
        var user = userRepository.findByTgId(update.getCallbackQuery().getFrom().getId()).orElseThrow();
        user.setState(State.AFK);
        userRepository.save(user);

        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        sendMessage.setText("Отменено!");

        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }

    private void doSendProcess(Update update) {
        var user = userRepository.findByTgId(update.getCallbackQuery().getFrom().getId()).orElseThrow();
        user.setState(State.SENDING);
        userRepository.save(user);

        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        sendMessage.setText("Отправьте файл: ");
        sendMessage.setReplyMarkup(MessageUtil.getKeyboardByPattern(1,1, List.of("Отмена"), List.of("cancel")));

        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }
}