// define a grammar called Hello
grammar Expr;

@header {
package jp.hashiwa.antlr4;
}

prog  : expr
      ;
expr  : term (('+'|'-') term)*
      ;
term  : unary (('*'|'/') unary)*
      ;
unary : factor
      | '-' factor
      ;
factor: INT
      | '(' expr ')'
      ;
NEWLINE : [\r\n]+ ;
INT     : [0-9]+ ;
