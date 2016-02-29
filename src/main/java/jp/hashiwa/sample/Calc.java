package jp.hashiwa.sample;

import jp.hashiwa.antlr4.ExprBaseListener;
import jp.hashiwa.antlr4.ExprLexer;
import jp.hashiwa.antlr4.ExprParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import javax.ws.rs.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Hashiwa on 2016/02/17.
 */
@Path("/calc")
public class Calc {
  private static final boolean DEBUG = Boolean.getBoolean(System.getProperty("jp.hashiwa.sample.debug", "false"));
  private static final String ERROR = "error";
  private static final ExecutorService executorService
          = Executors.newCachedThreadPool();

  protected static ExecutorService getExecutorService() {
    return executorService;
  }

  @GET
  @Consumes("text/plain")
  @Produces("text/plain")
  public String doGet(
          @QueryParam("exp") @DefaultValue("") String exp
  ) {
    String ret = calc(exp);
    printDebug(getClass().getSimpleName() + ": doGet: " + ret);
    return ret;
  }

//  @POST
//  @Consumes("text/plain")
//  @Produces("text/plain")
//  public String doPost(
//          @QueryParam("exp") @DefaultValue("") String exp
//  ) {
//    return calc(exp);
//  }

  protected String calc(String exp) {
    ExprLexer lexer = null;
    try {
      lexer = new ExprLexer(new ANTLRInputStream(new ByteArrayInputStream(exp.getBytes())));
    } catch (IOException e) {
      e.printStackTrace();
      return ERROR + " : " + e.getLocalizedMessage();
    }

    CommonTokenStream tokens = new CommonTokenStream(lexer);
    ExprParser parser = new ExprParser(tokens);
    ParseTree tree = parser.prog();
    ParseTreeWalker walker = new ParseTreeWalker();
    walker.walk(new ExprBaseListener(), tree);

    return calc(tree);
  }

  protected String calc(ParseTree tree) {
    String logPrefix = getClass().getSimpleName() + ": calc: ";

    printDebug(logPrefix + tree.getText());
    int len = tree.getChildCount();
    if (len == 0) {
      String msg = ERROR + " : invalid exp [" + tree.getText() + "]";
      printDebug(msg);
      return msg;
    } else if (len == 1) {
      ParseTree child = tree.getChild(0);
      String text = child.getText();
      try {
        // Number is terminal, so return the value.
        int intValue = Integer.parseInt(text);
        printDebug(logPrefix + "return int value : " + intValue);
        return Integer.toString(intValue);
      } catch(NumberFormatException e) {}

      if (text.matches("^-?\\d+$")) {
        // Invalid number because it is single term but cannot be parsed as Integer.
        String msg = ERROR + " : invalid int number [" + text + "]";
        printDebug(msg);
        return msg;
      }

      // Not number, parse the child.
      return calc(tree.getChild(0));
    } else {
      ParseTree firstChild = tree.getChild(0);
      String firstText = firstChild.getText();
      if ("(".equals(firstText)) {
        return parse(tree, 1, len-2);
      } else {
        return parse(tree, 0, len);
      }
    }
  }

  private String parse(ParseTree tree, int start, int offset) {
    String logPrefix = getClass().getSimpleName() + ": parse: ";

    ParseTree firstChild = tree.getChild(start);
    String firstText = firstChild.getText();
    printDebug(logPrefix + "firstText : " + firstText);
    Future<String> firstValue = resolve(firstText);

    List<String> operators = new ArrayList<>();
    List<Future<String>> values = new ArrayList<>();
    for (int i = start + 1; i < start + offset; i += 2) {
      ParseTree opChild = tree.getChild(i);
      String op = opChild.getText();
      printDebug(logPrefix + "operator : " + op);

      ParseTree anotherChild = tree.getChild(i+1);
      String anotherOne = anotherChild.getText();
      printDebug(logPrefix + "anotherOne : " + anotherOne);
      Future<String> anotherValue = resolve(anotherOne);

      operators.add(op);
      values.add(anotherValue);
    }

    try {
      int value = Integer.parseInt(firstValue.get());

      for (int j=0 ; j<operators.size() ; j++) {
        String op = operators.get(j);

        int another = Integer.parseInt(values.get(j).get());

        switch (op) {
          case "+": value += another; break;
          case "-": value -= another; break;
          case "*": value *= another; break;
          case "/": value /= another; break;
          default:
            throw new Error("invalid operator : " + op);
        }
      }

      return Integer.toString(value);
    } catch (NumberFormatException e) {
      printDebug(logPrefix, e);
      return ERROR + " : " + e.getLocalizedMessage();
    } catch (InterruptedException | ExecutionException e) {
      printDebug(logPrefix, e);
      return ERROR + " : " + e.getLocalizedMessage();
    }
  }

  /**
   * 式を解決する。
   * @param exp 式
   * @return 式を解決した結果を表すFutureオブジェクト
   */
  protected Future<String> resolve(String exp) {
    String logPrefix = getClass().getSimpleName() + ": resolve: ";
    printDebug(logPrefix + exp);
    return executorService.submit(() -> calc(exp));
  }

  protected void printDebug(int i) {
    printDebug(Integer.toString(i));
  }
  protected void printDebug(String s) {
    if (DEBUG) System.err.println("DEBUG " + Thread.currentThread().getName() + " : " + s);
  }
  protected void printDebug(String logPrefix, Throwable t) {
    if (DEBUG) {
      System.err.print(logPrefix);
      t.printStackTrace();
    }
  }

}
