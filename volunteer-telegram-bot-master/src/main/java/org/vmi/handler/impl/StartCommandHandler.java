package org.vmi.handler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.vmi.handler.UserRequestHandler;
import org.vmi.helper.KeyboardHelper;
import org.vmi.model.UserRequest;
import org.vmi.service.TelegramService;

import static org.vmi.constant.Constants.COMMAND;

@Component
public class StartCommandHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    public StartCommandHandler(TelegramService telegramService, KeyboardHelper keyboardHelper) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), COMMAND);
    }

    @Override
    public void handle(UserRequest request) {
        ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu();
        telegramService.sendMessage(request.getChatId(),
                "\uD83D\uDC4BПривіт! За допомогою цього чат-бота ви зможете зробити запит про допомогу!",
                replyKeyboard);
        telegramService.sendMessage(request.getChatId(),
                "Обирайте з меню нижче ⤵️");
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
