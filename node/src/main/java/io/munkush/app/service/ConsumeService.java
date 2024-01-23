package io.munkush.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static io.munkush.app.RabbitQueue.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumeService {

    private final TextService textService;
    private final PhotoService photoService;
    private final CallbackService callbackService;

    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessage(Update update) {
        log.info("consumed text: {}", update.getMessage().getText());
        textService.produce(update);
    }

    @RabbitListener(queues = CALLBACK_QUERY_UPDATE)
    public void consumeCallbackQuery(Update update) {
        log.info("consumed callbackQuery: {}", update.getCallbackQuery().getData());
        callbackService.produce(update);
    }

    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessage(Update update) {
        log.info("consumed photo: {}", update.getMessage().getPhoto());
        photoService.produce(update);
    }



}
