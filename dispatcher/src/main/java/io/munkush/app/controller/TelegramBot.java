package io.munkush.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.username}")
    public String botUsername;

    private final UpdateController updateController;

    public TelegramBot(@Lazy UpdateController updateController) {
        this.updateController = updateController;
    }

    @PostConstruct
    public void init(){
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }


    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
        log.info(String.valueOf(update.getUpdateId()));
    }
    public void sendMessage(SendMessage sendMessage){
        try {
            this.execute(sendMessage);
            log.info("executed message to: {}", sendMessage.getChatId());
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}
