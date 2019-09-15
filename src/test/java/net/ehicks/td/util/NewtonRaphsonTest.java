package net.ehicks.td.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class NewtonRaphsonTest
{
    @Test
    public void testBigSqrt()
    {
        BigDecimal result = NewtonRaphson.bigSqrt(new BigDecimal("16"));
        String message = "The sqrt of 16 must be 4. Instead it was " + result;
        Assertions.assertTrue((result.compareTo(new BigDecimal("4")) == 0), message);
    }
}
