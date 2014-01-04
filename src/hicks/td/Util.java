package hicks.td;

import java.math.BigDecimal;

public final class Util
{
    public static BigDecimal now()
    {
        return new BigDecimal(System.currentTimeMillis());
    }

    public static BigDecimal getElapsedTime(BigDecimal startTime)
    {
        return Util.now().subtract(startTime).divide(new BigDecimal("1000"));
    }
}
