package jp.hashiwa.sample;

import javax.ws.rs.*;

/**
 * Created by Hashiwa on 2016/02/10.
 */

@Path("/hello")
public class HelloRest {

  @GET
  @Consumes("text/plain")
  @Produces("text/plain")
  public String hello(
          @QueryParam("name") @DefaultValue("world") String name
  ) {
    return "Hello " + name + " !";
  }
}
