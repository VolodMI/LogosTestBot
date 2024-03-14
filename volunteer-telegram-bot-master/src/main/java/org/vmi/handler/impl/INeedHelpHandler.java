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

import java.util.List;

import static org.vmi.constant.Constants.I_NEED_HELP;
import static org.vmi.enums.ConversationState.WAITING_FOR_CITY;

@Component
public class INeedHelpHandler extends UserRequestHandler {

    public static List<String> cities = List.of("Київ", "Львів");

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public INeedHelpHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), I_NEED_HELP);
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildCitiesMenu(cities);
        telegramService.sendMessage(userRequest.getChatId(),"Оберіть місто або напишіть вручну⤵️", replyKeyboardMarkup);

        UserSession userSession = userRequest.getUserSession();
        userSession.setState(WAITING_FOR_CITY);
        userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

}
