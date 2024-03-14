package org.vmi.handler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.vmi.enums.ConversationState;
import org.vmi.handler.UserRequestHandler;
import org.vmi.helper.KeyboardHelper;
import org.vmi.model.UserRequest;
import org.vmi.model.UserSession;
import org.vmi.service.TelegramService;
import org.vmi.service.UserSessionService;

import static org.vmi.constant.Constants.*;
import static org.vmi.enums.ConversationState.WAITING_FOR_CITY;
import static org.vmi.enums.ConversationState.WAITING_FOR_TEXT;

@Component
public class CityEnteredHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public CityEnteredHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && WAITING_FOR_CITY.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        telegramService.sendMessage(userRequest.getChatId(),
                "✍️Тепер опишіть яка допомога вам потрібна⤵️",
                replyKeyboardMarkup);

        String city = userRequest.getUpdate().getMessage().getText();

        UserSession session = userRequest.getUserSession();
        session.setCity(city);
        session.setState(WAITING_FOR_TEXT);
        userSessionService.saveSession(userRequest.getChatId(), session);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

}
