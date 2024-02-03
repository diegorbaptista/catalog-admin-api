package com.codemagic.catalog.admin.application;

public abstract class UnaryUseCase<Input> {
    public abstract void execute(Input input);
}
