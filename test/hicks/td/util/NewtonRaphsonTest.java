package hicks.td.util;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class NewtonRaphsonTest
{
    @Test
    public void testBigSqrt() throws Exception
    {
        BigDecimal result = NewtonRaphson.bigSqrt(new BigDecimal("16"));
        Assert.assertTrue("The sqrt of 16 must be 4. Instead it was " + result, (result.compareTo(new BigDecimal("4")) == 0));
    }
}
