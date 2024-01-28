package com.codemagic.catalog.admin.application;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UseCaseTest {
    @Test
    void testNewUseCase() {
        var useCase = new UseCase();
        assertNotNull(useCase);
    }

}
