package org.vmi.model;

import lombok.Builder;
import lombok.Data;
import org.vmi.enums.ConversationState;

@Data
@Builder
public class UserSession {
    private Long chatId;
    private ConversationState state;
    private String city;
    private String text;
}
