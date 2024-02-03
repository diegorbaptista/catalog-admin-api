package com.codemagic.catalog.admin.application.category.create;

public record CreateCategoryCommand(
        String name,
        String description
) {
    public static CreateCategoryCommand with(String name, String description) {
        return new CreateCategoryCommand(name, description);
    }
}
