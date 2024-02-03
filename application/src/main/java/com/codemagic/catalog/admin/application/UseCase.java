package com.codemagic.catalog.admin.application;

import com.codemagic.catalog.admin.domain.category.Category;

public abstract class UseCase<Input, Output> {
    public abstract Output execute(Input input);
}