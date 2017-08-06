<img src="http://img.jcabi.com/logo-square.png" width="64px" height="64px" />

[![Managed by Zerocracy](http://www.zerocracy.com/badge.svg)](http://www.zerocracy.com)
[![DevOps By Rultor.com](http://www.rultor.com/b/jcabi/jcabi-w3c)](http://www.rultor.com/p/jcabi/jcabi-w3c)

[![Build Status](https://travis-ci.org/jcabi/jcabi-w3c.svg?branch=master)](https://travis-ci.org/jcabi/jcabi-w3c)
[![PDD status](http://www.0pdd.com/svg?name=jcabi/jcabi-w3c)](http://www.0pdd.com/p?name=jcabi/jcabi-w3c)
[![Build status](https://ci.appveyor.com/api/projects/status/nr4a6220d5e6awmc/branch/master?svg=true)](https://ci.appveyor.com/project/yegor256/jcabi-w3c/branch/master)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/com.jcabi/jcabi-w3c/badge.svg)](http://www.javadoc.io/doc/com.jcabi/jcabi-w3c)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-w3c/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-w3c)
[![Dependencies](https://www.versioneye.com/user/projects/561ac1a2a193340f2f0011a0/badge.svg?style=flat)](https://www.versioneye.com/user/projects/561ac1a2a193340f2f0011a0)

More details are here: [w3c.jcabi.com](http://w3c.jcabi.com/index.html).
Also, read this blog post: [W3C Java Validators](http://www.yegor256.com/2014/04/29/w3c-java-validators.html).

The library contains a few Java adapters to
[W3C online validators](http://validator.w3.org/nu/):

```java
import com.jcabi.w3c.HtmlValidator;
assert ValidatorBuilder.html().validate("<html>hello!</html>").valid();
```

## Questions?

If you have any questions about the framework, or something doesn't work as expected,
please [submit an issue here](https://github.com/jcabi/jcabi-w3c/issues/new).
If you want to discuss, please use our [Google Group](https://groups.google.com/forum/#!forum/jcabi).

## How to contribute?

Fork the repository, make changes, submit a pull request.
We promise to review your changes same day and apply to
the `master` branch, if they look correct.

Please run Maven build before submitting a pull request:

```
$ mvn clean install -Pqulice
```
