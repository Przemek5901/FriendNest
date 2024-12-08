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
@Table(name = "\"MESSAGE\"")
public class Message {
    @Id
    @Column(name = "\"MESSAGE_ID\"", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"CHAT_ID\"", nullable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"SENDER_ID\"", nullable = false)
    private User sender;

    @Column(name = "\"CONTENT\"")
    private String content;

    @Column(name = "\"IMAGE_URL\"")
    private String imageUrl;

    @Column(name = "\"CREATED_AT\"", nullable = false)
    private OffsetDateTime createdAt;

    @Transient
    private String imageBase64;

}