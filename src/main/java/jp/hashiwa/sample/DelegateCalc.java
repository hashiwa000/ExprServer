package jp.hashiwa.sample;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.Future;

/**
 * 計算の一部を他サーバーに移譲するクラス。
 *
 * Created by Hashiwa on 2016/02/25.
 */

@Path("/delegatecalc")
public class DelegateCalc extends Calc {
  private String path = "delegatecalc";
  private String hostname = "localhost";
  private int port = 8080;

  @GET
  @Consumes("text/plain")
  @Produces("text/plain")
  public String doGet(
          @QueryParam("exp") @DefaultValue("") String exp
  ) {
    String ret = calc(exp);
    printDebug("doGet = " + ret);
    return ret;
  }

  /**
   * see: https://jersey.java.net/documentation/latest/client.html#d0e4257
   * @param exp
   * @return
   */
  @Override
  protected Future<String> resolve(String exp) {
    printDebug("send     : " + exp + " ...");
    return getExecutorService().submit(() -> {

      Client client = ClientBuilder.newClient();
      WebTarget webTarget = client
              .target(getDelegateURLStr())
              .queryParam("exp", exp);
      Invocation.Builder invocationBuilder =
              webTarget.request(MediaType.TEXT_PLAIN_TYPE);
      Response response = invocationBuilder.get();
      int responseStatus = response.getStatus();
      String responseText = response.readEntity(String.class);
      printDebug("response : " + responseStatus);
      printDebug("resolved : " + exp + " = " + responseText);

      return responseText;
    });
  }

  private String getDelegateURLStr() {
    return "http://" +
            getDelegateHost() + ":" + getDelegatePort() +
            "/hello-rest/rest/" + getDelegatePath();
  }

  private String getDelegatePath() {
    return path;
  }

  private String getDelegateHost() {
    return hostname;
  }

  private int getDelegatePort() {
    return port;
  }
}
