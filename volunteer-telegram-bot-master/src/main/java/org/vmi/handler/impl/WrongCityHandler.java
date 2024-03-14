package org.vmi.handler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.vmi.handler.UserRequestHandler;
import org.vmi.helper.KeyboardHelper;
import org.vmi.model.UserRequest;
import org.vmi.model.UserSession;
import org.vmi.service.TelegramService;
import org.vmi.service.UserSessionService;

import static org.vmi.constant.Constants.AREAL;
import static org.vmi.enums.ConversationState.WAITING_FOR_CITY;
import static org.vmi.handler.impl.INeedHelpHandler.cities;

@Component
public class WrongCityHandler extends UserRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public WrongCityHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && WAITING_FOR_CITY.equals(userRequest.getUserSession().getState())
                && !AREAL.contains(userRequest.getUpdate().getMessage().getText());
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCitiesMenu(cities);
        telegramService.sendMessage(userRequest.getChatId(),
                "На жаль, ми ще не працюємо у вказаному місті. Оберіть із меню нижче⤵️", replyKeyboardMarkup);
        UserSession userSession = userRequest.getUserSession();
        System.out.println(AREAL);
        userSession.setState(WAITING_FOR_CITY);
        userSessionService.saveSession(userSession.getChatId(), userSession);

    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
