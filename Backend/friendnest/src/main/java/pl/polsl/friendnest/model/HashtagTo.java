package pl.polsl.friendnest.model;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashtagTo {
    private Hashtag hashtag;
    private Integer hashtagCount;
}
