package com.codemagic.catalog.admin.domain;

import com.codemagic.catalog.admin.domain.category.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class CategoryTest {
    @Test
    void testNewCategory() {
        var category = new Category();
        assertNotNull(category);
    }
}
