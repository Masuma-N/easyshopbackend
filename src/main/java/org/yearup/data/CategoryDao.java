package org.yearup.data;

import org.yearup.models.Category;

import java.util.List;
import org.springframework.http.ResponseEntity;

public interface CategoryDao
{
    List<Category> getAllCategories();
    Category getById(int categoryId);
    ResponseEntity<Category> create(Category category);

    void update(int categoryId, Category category);
    void delete(int categoryId);
}
