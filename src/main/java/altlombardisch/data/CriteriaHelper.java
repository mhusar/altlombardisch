package altlombardisch.data;

import altlombardisch.siglum.Siglum;
import altlombardisch.siglum.SiglumType;
import org.apache.wicket.model.ResourceModel;

import javax.persistence.criteria.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A helper class for criteria restrictions.
 */
final class CriteriaHelper {
    /**
     * Matches a filter string against a pos source type.
     *
     * @param filter string filter
     * @return A pos source type, or null.
     */
    private static SiglumType.Type matchSiglumType(String filter) {
        String primaryTypeString = new ResourceModel("Type.PRIMARY").getObject();
        String secondaryTypeString = new ResourceModel("Type.SECONDARY").getObject();
        String tertiaryTypeString = new ResourceModel("Type.TERTIARY").getObject();

        if (primaryTypeString.toUpperCase().startsWith(filter.toUpperCase())) {
            return SiglumType.Type.PRIMARY;
        } else if (secondaryTypeString.toUpperCase().startsWith(filter.toUpperCase())) {
            return SiglumType.Type.SECONDARY;
        } else if (tertiaryTypeString.toUpperCase().startsWith(filter.toUpperCase())) {
            return SiglumType.Type.TERTIARY;
        }

        return null;
    }

    /**
     * Returns automatically created pos restrictions for a string filter.
     *
     * @param criteriaBuilder constructor for criteria queries
     * @param root            query root referencing entities
     * @param filter          string filter
     * @return An expression of type boolean, or null.
     */
    private static Expression<Boolean> getSiglumFilterStringRestriction(CriteriaBuilder criteriaBuilder, Root<?> root,
                                                                     String filter) {
        SiglumType.Type type = CriteriaHelper.matchSiglumType(filter);

        if (type != null) {
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("name"), filter + "%"),
                    criteriaBuilder.equal(root.get("type"), type));
        } else {
            return criteriaBuilder.like(root.get("name"), filter + "%");
        }
    }

    /**
     * Returns automatically created restrictions for a string filter.
     *
     * @param criteriaBuilder constructor for criteria queries
     * @param root            query root referencing entities
     * @param joins           map of joins
     * @param filter          string filter
     * @param typeClass       data type
     * @return An expression of type boolean, or null.
     */
    public static Expression<Boolean> getFilterStringRestriction(CriteriaBuilder criteriaBuilder, Root<?> root,
                                                                 Map<String, Join<?, ?>> joins, String filter,
                                                                 String property, Class<?> typeClass) {
        if (typeClass.equals(Siglum.class)) {
            return getSiglumFilterStringRestriction(criteriaBuilder, root, filter);
        }

        return null;
    }

    /**
     * Returns an automatically created list of order objects for a property string.
     *
     * @param criteriaBuilder constructor for criteria queries
     * @param root            query root referencing entities
     * @param joins           map of joins
     * @param property        sort property
     * @param isAscending     sort direction
     * @param typeClass       data type
     * @return A list of order objects.
     */
    public static List<Order> getOrder(CriteriaBuilder criteriaBuilder, Root<?> root, Map<String, Join<?, ?>> joins,
                                       String property, Boolean isAscending, Class<?> typeClass) {
        List<Order> orderList = new ArrayList<>();
        String[] splitProperty = property.split("\\.");
        Expression<String> expression;

        if (Array.getLength(splitProperty) == 2) {
            Join<?, ?> join = joins.get(splitProperty[0]);

            if (join != null) {
                expression = join.get(splitProperty[1]);
            } else {
                throw new IllegalStateException("Join for sort property " + property + " is missing.");
            }
        } else {
            expression = root.get(property);
        }

        if (isAscending) {
            orderList.add(criteriaBuilder.asc(expression));
        } else {
            orderList.add(criteriaBuilder.desc(expression));
        }

        return orderList;
    }

    /**
     * Returns automatically created joins for some classes.
     *
     * @param root      query root referencing entities
     * @param typeClass data type
     * @return A map of joins, or null.
     */
    @SuppressWarnings("SameReturnValue")
    public static Map<String, Join<?, ?>> getJoins(Root<?> root, Class<?> typeClass) {
        return null;
    }
}
