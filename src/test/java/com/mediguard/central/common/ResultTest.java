package com.mediguard.central.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void testSuccess() {
        Result<String> result = Result.success("test data");
        assertEquals(200, result.getCode());
        assertEquals("success", result.getMessage());
        assertEquals("test data", result.getData());
    }

    @Test
    void testError() {
        Result<Void> result = Result.error(500, "error message");
        assertEquals(500, result.getCode());
        assertEquals("error message", result.getMessage());
        assertNull(result.getData());
    }
}
