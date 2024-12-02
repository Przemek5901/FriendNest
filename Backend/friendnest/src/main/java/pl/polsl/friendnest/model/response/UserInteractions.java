package pl.polsl.friendnest.model.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserInteractions {

    private Integer commentsNumber;
    private Integer likesNumber;
    private Integer dislikesNumber;
    private Integer repostsNumber;
    private Integer quotesNumber;
    @JsonProperty("isLiked")
    private boolean isLiked;
    @JsonProperty("isCommented")
    private boolean isCommented;
    @JsonProperty("isDisliked")
    private boolean isDisliked;
    @JsonProperty("isReposted")
    private boolean isReposted;
    @JsonProperty("isQuoted")
    private boolean isQuoted;

}
