package pl.polsl.friendnest.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"CHAT\"")
public class Chat {
    @Id
    @Column(name = "\"CHAT_ID\"", nullable = false)
    private Integer chatId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"USER_1\"", nullable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"USER_2\"", nullable = false)
    private User user2;

    @Column(name = "\"CREATED_AT\"")
    private OffsetDateTime createdAt;

}