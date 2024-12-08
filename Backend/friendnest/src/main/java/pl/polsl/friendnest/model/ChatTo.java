package pl.polsl.friendnest.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatTo {
    private Chat chat;
    private Message lastMessage;
}
