package com.example.demo.specification;

public class PostSpecs{
//
//    public static Specification<Post> search(FilterRequest obj) {
//        return (root, query, builder) -> {
//            List<Predicate> conditions = new ArrayList<>();
//
//            List<TAGS> tagsList = Arrays.asList(
//                    TAGS.DISCUSS,
//                    TAGS.EXPERIENCES,
//                    TAGS.INFORMATION);
//
//            if (!CollectionUtils.isEmpty(obj.getFilterList()) && obj.getFilterList() != null) {
//                Set<TAGS> checkTagList = new HashSet<>();
//                obj.getFilterList().forEach(item -> {
//                            if (tagsList.contains(TAGS.valueOf(item))) {
//                                checkTagList.add(TAGS.valueOf(item));
//                            }
//                        }
//                );
//                conditions.add(root.get(Post_.TAGS_LIST).in(checkTagList));
//            } else {
//                conditions.add(root.get(Post_.TAGS_LIST).in(tagsList));
//            }
//
//            query.orderBy(builder.desc(root.get(Post_.TOTAL_LIKE)));
//            return builder.and(conditions.toArray(new Predicate[0]));
//        };
//    }

//    public static Specification<MobileKit> searchKitName(OrderFilterRequest obj) {
//        return (root, query, builder) -> {
//            List<Predicate> conditions = new ArrayList<>();
//
//            if (!Strings.isEmpty(obj.getKey())) {
//                conditions.add(builder.like(builder.upper(root.get(MobileKit_.KIT_NAME)), "%" + obj.getKey().toUpperCase() + "%"));
//            }
//
//            query.orderBy(builder.asc(root.get(MobileKit_.CREATED_AT)));
//            return builder.and(conditions.toArray(new Predicate[0]));
//        };
//    }
}
