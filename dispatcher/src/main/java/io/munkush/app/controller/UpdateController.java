package io.munkush.app.controller;

import io.munkush.app.service.UpdateProduceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static io.munkush.app.RabbitQueue.*;

@Component
@RequiredArgsConstructor
public class UpdateController {

    private final TelegramBot telegramBot;
    private final UpdateProduceService produceService;
    public void processUpdate(Update update) {
        if(update.hasCallbackQuery()){
            produceService.produce(CALLBACK_QUERY_UPDATE, update);
        } else if(update.hasMessage()) {
            Message message = update.getMessage();
            if(message.hasText()){
                produceService.produce(TEXT_MESSAGE_UPDATE, update);
            } else if(message.hasPhoto()){
                produceService.produce(PHOTO_MESSAGE_UPDATE, update);
            } else if(message.hasDocument()){
                produceService.produce(DOC_MESSAGE_UPDATE, update);
            }
        } else {
            sendMessage(update.getMessage().getChatId(), "error");
        }
    }


    private void sendMessage(long chatId, String text){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        telegramBot.sendMessage(sendMessage);
    }

    public void sendMessage(SendMessage sendMessage){
        telegramBot.sendMessage(sendMessage);
    }


}
