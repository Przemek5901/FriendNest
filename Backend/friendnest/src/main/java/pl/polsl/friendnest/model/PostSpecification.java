package pl.polsl.friendnest.model;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;


public class PostSpecification {

    public static Specification<Post> userNot(User user) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("user"), user);
    }

    public static Specification<Post> category(Integer category) {
        return (root, query, criteriaBuilder) -> {
            if (category == 0) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    public static Specification<Post> sortByCriteria(Integer sortOption) {
        return (root, query, criteriaBuilder) -> {
            Join<Post, Interaction> interactions = root.join("interactions", JoinType.LEFT);

            switch (sortOption) {
                case 1:
                    query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
                    break;

                case 2:
                    query.orderBy(criteriaBuilder.asc(root.get("createdAt")));
                    break;

                case 3:
                    query.groupBy(root.get("postId"));
                    query.orderBy(criteriaBuilder.desc(
                            criteriaBuilder.sum(
                                    criteriaBuilder.selectCase()
                                            .when(criteriaBuilder.equal(interactions.get("interactionType"), 2), 1)
                                            .otherwise(0)
                                            .as(Integer.class)
                            )
                    ));
                    break;

                case 4:
                    query.groupBy(root.get("postId"));
                    query.orderBy(criteriaBuilder.asc(
                            criteriaBuilder.sum(
                                    criteriaBuilder.selectCase()
                                            .when(criteriaBuilder.equal(interactions.get("interactionType"), 2), 1)
                                            .otherwise(0)
                                            .as(Integer.class)
                            )
                    ));
                    break;

                default:
                    assert query != null;
                    query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
                    break;
            }

            return null;
        };
    }
}
