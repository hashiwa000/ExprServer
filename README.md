# ExprServer

## できること

ウェブブラウザ上で以下のような四則演算ができます。

    1+(9*4-3)+4/2

## 実行方法

```
>set JAVA_HOME=<JDK8_HOME>
>gradlew jettyRunWar
:generateGrammarSource
:compileJava
:processResources
:classes
:init
:war
:jettyRunWar
```

ブラウザで http://localhost:8080/hello-rest/ にアクセスします。
別マシンのブラウザを使う場合は、localhostの代わりにサーバーのIPアドレスやホスト名を入れます。
