package io.munkush.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestController
@RequiredArgsConstructor
public class UploadController {
    private final TelegramBot telegramBot;

    @GetMapping("/getPhoto/{photoId}")
    public File getPhoto(@PathVariable String photoId){
        try {
            return telegramBot.execute(new GetFile(photoId));
        } catch (TelegramApiException e) {
            return null;
        }
    }

}
