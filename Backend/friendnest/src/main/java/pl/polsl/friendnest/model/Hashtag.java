package pl.polsl.friendnest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "\"HASHTAG\"")
public class Hashtag {
    @Id
    @Column(name = "\"HASHTAG_ID\"", nullable = false)
    private Integer id;

    @Column(name = "\"TAG\"", nullable = false, length = 30)
    private String tag;

}