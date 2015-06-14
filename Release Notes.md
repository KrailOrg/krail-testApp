### Release Notes for krail-testApp 0.7.10

Tests for krail 0.9.5.  Also replaces the DefaultRealm with a more specific uac set of components

#### Change log

-   [411](https://github.com/davidsowerby/krail-testApp/issues/411): Invalid issue number or uri.   https://api.github.com/repos/davidsowerby/krail-testApp/issues/411


#### Dependency changes

   compile dependency version changed to: krail-jpa:0.8.10
   compile dependency version changed to: krail:0.9.5
   test compile dependency version changed to: krail-bench:0.8.0

#### Detail

*Removed unnecessary closeNotification() - which has also strangely lost its ability to close*


---
*Updated version information*


---
*Merge branch 'develop' of https://github.com/davidsowerby/krail-testApp into develop*


---
*Tests for [krail 423](https://github.com/davidsowerby/krail/issues/423)*


---
*See [krail 411](https://github.com/davidsowerby/krail/issues/411) Login status panel shows correct state*


---
*Fix [411](https://github.com/davidsowerby/krail-testApp/issues/411) Login status panel shows correct state*


---
*Vaadin 7.4.8*


---
*Replaces DefaultRealm with TestAppRealm and TrivialCredentialsStore, TrivialUserAccount for more flexible access control testing*
Tests for new Shiro annotation MethodInterceptors.  Removed paus() statements, as change to krail-bench removes the need for most of them


---
*Upgrade to Vaadin 7.4.7*


---
