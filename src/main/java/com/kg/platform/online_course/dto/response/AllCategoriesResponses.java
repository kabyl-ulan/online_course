package com.kg.platform.online_course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllCategoriesResponses {
    private Long id;
    private String categoryName;
    private List<AllCategoriesResponses> subCategoryResponses;
}
