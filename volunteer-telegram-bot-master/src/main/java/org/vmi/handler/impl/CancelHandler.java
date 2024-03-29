package org.vmi.handler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.vmi.handler.UserRequestHandler;
import org.vmi.helper.KeyboardHelper;
import org.vmi.model.UserRequest;
import org.vmi.model.UserSession;
import org.vmi.service.TelegramService;
import org.vmi.service.UserSessionService;

import static org.vmi.constant.Constants.BTN_CANCEL;
import static org.vmi.enums.ConversationState.CONVERSATION_STARTED;

@Component
public class CancelHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public CancelHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), BTN_CANCEL);
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu();
        telegramService.sendMessage(userRequest.getChatId(),
                "Обирайте з меню нижче ⤵️", replyKeyboard);

        UserSession userSession = userRequest.getUserSession();
        userSession.setState(CONVERSATION_STARTED);
        userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
