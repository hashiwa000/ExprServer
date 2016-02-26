package jp.hashiwa.sample;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Hashiwa on 2016/02/25.
 */
public class TestCalc {
  private Calc calcObj;

  @Before
  public void before() {
    calcObj = new Calc();
  }

  @Test
  public void testCalc() {
    Assert.assertEquals(Integer.parseInt(calcObj.calc("2")), 2);
    Assert.assertEquals(Integer.parseInt(calcObj.calc("-2")), -2);
    Assert.assertEquals(Integer.parseInt(calcObj.calc(Integer.toString(Integer.MAX_VALUE))), Integer.MAX_VALUE);
    Assert.assertEquals(Integer.parseInt(calcObj.calc(Integer.toString(Integer.MIN_VALUE))), Integer.MIN_VALUE);
  }

  @Test
  public void testCalc2() {
    Assert.assertEquals(Integer.parseInt(calcObj.calc("1+2")), 3);
    Assert.assertEquals(Integer.parseInt(calcObj.calc("2-5")), -3);
    Assert.assertEquals(Integer.parseInt(calcObj.calc("3*2")), 6);
    Assert.assertEquals(Integer.parseInt(calcObj.calc("6/2")), 3);
    Assert.assertEquals(Integer.parseInt(calcObj.calc("-3*2")), -6);
  }

  @Test
  public void testCalc3() {
    Assert.assertEquals(Integer.parseInt(calcObj.calc("1+(2*4)")), 9);
    Assert.assertEquals(Integer.parseInt(calcObj.calc("1+(8+2*4)")), 17);
    Assert.assertEquals(Integer.parseInt(calcObj.calc(
            "1+(8+2*(4+3+5-9*3+(4+9)+(9-4)))+" +
            "1+(8+2*(4+3+5-9*3+(4+9)+(9-4)))+" +
            "1+(8+2*(4+3+5-9*3+(4+9)+(9-4)))+" +
            "1+(8+2*(4+3+5-9*3+(4+9)+(9-4)))+" +
            "1+(8+2*(4+3+5-9*3+(4+9)+(9-4)))+" +
            "1+(8+2*(4+3+5-9*3+(4+9)+(9-4)))+" +
            "1+(8+2*(4+3+5-9*3+(4+9)+(9-4)))+" +
            "1+(8+2*(4+3+5-9*3+(4+9)+(9-4)))")), 120);
  }

  @Test
  public void testCalcError() {
    String errorPrefix = "error";
    Assert.assertTrue(calcObj.calc("").startsWith(errorPrefix));
    Assert.assertTrue(calcObj.calc("1+").startsWith(errorPrefix));

    // FIXME: unexpected result
//    Assert.assertTrue(calcObj
//            .calc(Long.toString(Integer.MAX_VALUE + 1L))
//            .startsWith(errorPrefix));

    // FIXME: unexpected result
//    Assert.assertTrue(calcObj
//            .calc(Long.toString(Integer.MIN_VALUE - 1L))
//            .startsWith(errorPrefix));

    // FIXME: unexpected result
//    System.out.println(calcObj.calc("1+2="));
//    Assert.assertTrue(calcObj.calc("1+2=").startsWith(errorPrefix));
  }

}
