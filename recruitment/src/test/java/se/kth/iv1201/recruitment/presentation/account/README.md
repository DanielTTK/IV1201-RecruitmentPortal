Explanations (kept here so develoepers have context and so we can refer back to them when writing future tests):
 
  - SpringBootTest: boots the full Spring application for tests. This allows
    wiring controllers, services, repositories and exercising the
    application the way it runs in main, but for testing.
 
  - WebApplicationContext: the web-specific Spring ApplicationContext that holds
    web configuration (controllers, HandlerMappings, view resolvers etc). We
    build MockMvc from this context so the test uses the same web stack that
    Spring would use at runtime.
 
  - MockMvc: a testing utility that lets us perform HTTP like requests on
    Spring MVC stack without starting a HTTP server. It sends requests
    through DispatcherServlet, runs relevant methods and draws views.
 
  - springSecurity(): part of `spring-security-test`. It's a MockMvc configurer that
    applies the Spring Security filter chain to MockMvc so security-related behavior
    (authentication, CSRF protection, authorization) is consistent to main during tests.
 
  - csrf(): a request processor provided by `spring-security-test` that adds
    valid CSRF tokens to the requests. When CSRF protection is enabled in security,
    POST/PUT/DELETE requests require a token; `csrf()` simulates that
    token so the request arent rejected.
 
  - model(): a MockMvcResultMatchers helper that inspects the MVC Model produced by
    controller methods (attributes added with @ModelAttribute).
    `model().attributeHasFieldErrors("registerForm", "confirmedPassword")` checks
    that the model contains binding/validation errors on the `confirmedPassword` field
    of the `registerForm` object — that's how controller-level `BindingResult` errors
    are exposed to templates/views.