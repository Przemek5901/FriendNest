package pl.polsl.friendnest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class PostHashtagId implements Serializable {
    private static final long serialVersionUID = 3568881560704105877L;
    @Column(name = "\"HASHTAG_ID\"", nullable = false)
    private Integer hashtagId;

    @Column(name = "\"POST_ID\"", nullable = false)
    private Integer postId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PostHashtagId entity = (PostHashtagId) o;
        return Objects.equals(this.hashtagId, entity.hashtagId) &&
                Objects.equals(this.postId, entity.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashtagId, postId);
    }

}