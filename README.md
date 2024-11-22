This repository has been created to describe the regression issue created by Spring Boot 3.3.6.

Test calls endpoint serving plain text and checks the Content-Length header.
This header is useful if implementation wants to fetch a subset of the content (e.g. from the byte range x to y), and implementation wants to identify the current content size bytes without fetching the actual content. 
2 tests are created - one fetches headers via GET and another fetches the headers using HEAD.

# Behaviour in Spring Boot 3.3.5
Content-Length header is received both using GET and HEAD requests.
```bash
./gradlew clean test -i
...
TextControllerIntegrationTest > GETshouldHaveContentLengthHeaderInTheResponseHeaders() STANDARD_OUT
    2024-11-22 20:57:18.540 [Test worker] INFO  e.b.t.c.l.i.TextControllerIntegrationTest - Application endpoint: http://localhost:57188/text
    2024-11-22 20:57:18.577 [http-nio-auto-1-exec-1] INFO  o.a.c.c.C.[Tomcat].[localhost].[/] - Initializing Spring DispatcherServlet 'dispatcherServlet'
    2024-11-22 20:57:18.577 [http-nio-auto-1-exec-1] INFO  o.s.web.servlet.DispatcherServlet - Initializing Servlet 'dispatcherServlet'
    2024-11-22 20:57:18.578 [http-nio-auto-1-exec-1] INFO  o.s.web.servlet.DispatcherServlet - Completed initialization in 1 ms
    2024-11-22 20:57:18.593 [Test worker] INFO  e.b.t.c.l.i.TextControllerIntegrationTest - Received headers: [Content-Type:"text/plain;charset=UTF-8", Content-Length:"1694", Date:"Fri, 22 Nov 2024 20:57:18 GMT", Keep-Alive:"timeout=60", Connection:"keep-alive"]

TextControllerIntegrationTest > HEADshouldHaveContentLengthHeaderInTheResponseHeaders() STANDARD_OUT
    2024-11-22 20:57:18.618 [Test worker] INFO  e.b.t.c.l.i.TextControllerIntegrationTest - Application endpoint: http://localhost:57188/text
    2024-11-22 20:57:18.620 [Test worker] INFO  e.b.t.c.l.i.TextControllerIntegrationTest - Received headers: [Content-Type:"text/plain;charset=UTF-8", Content-Length:"1694", Date:"Fri, 22 Nov 2024 20:57:18 GMT", Keep-Alive:"timeout=60", Connection:"keep-alive"]
```

# Behavior in Spring Boot 3.3.6
Content-Length header is received only using GET request, HEAD doesn't return Content-Length header:
```bash
./gradlew clean test -i
....
TextControllerIntegrationTest > GETshouldHaveContentLengthHeaderInTheResponseHeaders() STANDARD_OUT
    2024-11-22 20:58:31.303 [Test worker] INFO  e.b.t.c.l.i.TextControllerIntegrationTest - Application endpoint: http://localhost:57335/text
    2024-11-22 20:58:31.340 [http-nio-auto-1-exec-1] INFO  o.a.c.c.C.[Tomcat].[localhost].[/] - Initializing Spring DispatcherServlet 'dispatcherServlet'
    2024-11-22 20:58:31.340 [http-nio-auto-1-exec-1] INFO  o.s.web.servlet.DispatcherServlet - Initializing Servlet 'dispatcherServlet'
    2024-11-22 20:58:31.341 [http-nio-auto-1-exec-1] INFO  o.s.web.servlet.DispatcherServlet - Completed initialization in 1 ms
    2024-11-22 20:58:31.361 [Test worker] INFO  e.b.t.c.l.i.TextControllerIntegrationTest - Received headers: [Content-Type:"text/plain;charset=UTF-8", Content-Length:"1694", Date:"Fri, 22 Nov 2024 20:58:31 GMT", Keep-Alive:"timeout=60", Connection:"keep-alive"]

TextControllerIntegrationTest > HEADshouldHaveContentLengthHeaderInTheResponseHeaders() STANDARD_OUT
    2024-11-22 20:58:31.389 [Test worker] INFO  e.b.t.c.l.i.TextControllerIntegrationTest - Application endpoint: http://localhost:57335/text
    2024-11-22 20:58:31.394 [Test worker] INFO  e.b.t.c.l.i.TextControllerIntegrationTest - Received headers: [Content-Type:"text/plain;charset=UTF-8", Date:"Fri, 22 Nov 2024 20:58:31 GMT", Keep-Alive:"timeout=60", Connection:"keep-alive"]

TextControllerIntegrationTest > HEADshouldHaveContentLengthHeaderInTheResponseHeaders() FAILED
    org.opentest4j.AssertionFailedError: 
    expected: 1694L
     but was: -1L
        at java.base@17.0.12/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
        at java.base@17.0.12/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:77)
        at java.base@17.0.12/jdk.internal.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
        at java.base@17.0.12/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:500)
        at app//eu.blulic.test.content.length.issue.TextControllerIntegrationTest.HEADshouldHaveContentLengthHeaderInTheResponseHeaders(TextControllerIntegrationTest.java:61)
```

