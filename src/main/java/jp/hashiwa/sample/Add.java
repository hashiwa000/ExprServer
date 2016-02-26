package jp.hashiwa.sample;

import javax.ws.rs.*;

/**
 * Created by Hashiwa on 2016/02/17.
 */
@Path("/add")
public class Add {

  @GET
  @Consumes("text/plain")
  @Produces("text/plain")
  public String hello(
          @QueryParam("value1") String valueStr1,
          @QueryParam("value2") String valueStr2
  ) {
    if (valueStr1==null || valueStr2==null) {
      return "error";
    }

    try {
      double value1 = Double.parseDouble(valueStr1);
      double value2 = Double.parseDouble(valueStr2);
      return (value1 + value2) + "";
    } catch(NumberFormatException e) {
      return "error";
    }
  }
}
