<img src="http://img.jcabi.com/logo-square.png" width="64px" height="64px" />

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![Managed by Zerocracy](https://www.0crat.com/badge/C3RUBL5H9.svg)](https://www.0crat.com/p/C3RUBL5H9)
[![DevOps By Rultor.com](http://www.rultor.com/b/jcabi/jcabi-w3c)](http://www.rultor.com/p/jcabi/jcabi-w3c)

[![PDD status](http://www.0pdd.com/svg?name=jcabi/jcabi-w3c)](http://www.0pdd.com/p?name=jcabi/jcabi-w3c)
[![Javadoc](https://javadoc.io/badge/com.jcabi/jcabi-w3c.svg)](http://www.javadoc.io/doc/com.jcabi/jcabi-w3c)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-w3c/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-w3c)

More details are here: [w3c.jcabi.com](http://w3c.jcabi.com/index.html).
Also, read this blog post: [W3C Java Validators](http://www.yegor256.com/2014/04/29/w3c-java-validators.html).

The library contains a few Java adapters to
[W3C online validators](http://validator.w3.org/nu/):

```java
import com.jcabi.w3c.HtmlValidator;
assert ValidatorBuilder.html().validate("<html>hello!</html>").valid();
```

## How to contribute?

Fork the repository, make changes, submit a pull request.
We promise to review your changes same day and apply to
the `master` branch, if they look correct.

Please run Maven build before submitting a pull request:

```
$ mvn clean install -Pqulice
```
