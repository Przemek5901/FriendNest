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
public class BlockedUserId implements Serializable {
    private static final long serialVersionUID = -3995808746922674215L;
    @Column(name = "\"BLOCKER_ID\"", nullable = false)
    private Integer blockerId;

    @Column(name = "\"BLOCKED_ID\"", nullable = false)
    private Integer blockedId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BlockedUserId entity = (BlockedUserId) o;
        return Objects.equals(this.blockerId, entity.blockerId) &&
                Objects.equals(this.blockedId, entity.blockedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockerId, blockedId);
    }

}