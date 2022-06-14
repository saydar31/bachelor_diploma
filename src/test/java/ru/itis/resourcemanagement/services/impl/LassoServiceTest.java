package ru.itis.resourcemanagement.services.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LassoServiceTest {

    @Autowired
    private LassoService lassoService;

    @Test
    public void test(){
        lassoService.recalculateCoefficients(1L);
    }

}