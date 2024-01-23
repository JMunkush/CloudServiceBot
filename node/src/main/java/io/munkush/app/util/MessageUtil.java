package io.munkush.app.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class MessageUtil {
    public static String GREETING = "Привет !\nЗдесь можете хранить файлы, а также смотреть файлы других пользователей \uD83D\uDE07";
    public static InlineKeyboardMarkup getKeyBoardByRow(int row, List<String> args, List<String> callbacks){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();

        int counter = 0;
        for (int i = 0; i < args.size(); i++) {
            List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
            for (int j = 0; j < row; j++) {
                if (counter == args.size())
                    break;
                InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
                keyboardButton.setText(args.get(counter));
                keyboardButton.setCallbackData(callbacks.get(counter));
                keyboardButtons.add(keyboardButton);
                counter++;
            }
            inlineKeyboardButtons.add(keyboardButtons);
        }
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);

        return inlineKeyboardMarkup;
    }
    public static InlineKeyboardMarkup getKeyboardByPattern(int first, int second, List<String> args, List<String> callbacks){

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        int counter = 0;
        for(int i = 0; i < second; i++){
            List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
            for (int j = 0; j < first; j++){
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(args.get(counter));
                inlineKeyboardButton.setCallbackData(callbacks.get(counter));
                keyboardButtons.add(inlineKeyboardButton);
                counter++;
            }
            inlineKeyboardButtons.add(keyboardButtons);
        }
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);

        return inlineKeyboardMarkup;
    }
}
