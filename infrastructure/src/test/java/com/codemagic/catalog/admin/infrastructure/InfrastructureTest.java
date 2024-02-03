package com.codemagic.catalog.admin.infrastructure;

import com.codemagic.catalog.admin.application.UseCase;
import com.codemagic.catalog.admin.domain.category.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InfrastructureTest {
    @Test
    void testNewInfrastructure() {
        var category = Category.newCategory("", "");
        Assertions.assertNotNull(category);
    }
}
