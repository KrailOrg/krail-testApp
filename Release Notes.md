### Release Notes for krail-testApp 0.8.1

Tests for Krail 0.9.7.  Vaadin 7.5.10 

#### Change log

-   [8](https://github.com/davidsowerby/krail-testApp/issues/8): Intermittent test failure, LocaleTest
-   [485](https://github.com/davidsowerby/krail-testApp/issues/485): Invalid issue number or uri.   https://api.github.com/repos/davidsowerby/krail-testApp/issues/485


#### Dependency changes

   compile dependency version changed to: krail:0.9.7
   test compile dependency version changed to: krail-bench:0.8.2
   compile dependency version changed to: krail-jpa:0.9.1

#### Detail

*Updated version and version description*


---
*Tests amended for [krail 486](https://github.com/davidsowerby/krail/issues/486)*


---
*Fix [485](https://github.com/davidsowerby/krail-testApp/issues/485) Use event bus providers*

Replaced all cases of @UIBus, @SessionBus and @GlobalBus annotated constructor parameters with UOIBusProvider, SessionBusProvider and GlobalBusProvider


---
[krail 482](https://github.com/*davidsowerby/krail/issues/482) Vaadin 7.5.10*


---
*Vaadin 7.5.9*


---
*See [krail 477](https://github.com/davidsowerby/krail/issues/477) Vaadin 7.5.8*


---
*See [krail 474](https://github.com/davidsowerby/krail/issues/474) Vaadin 7.5.7*

Upgrade to Vaadin 7.5.7


---
*Fix [8](https://github.com/davidsowerby/krail-testApp/issues/8) Intermittent test failure LocaleTest*

Changes to tests:
- Adjusted timeout for navigating to URL (probably made no difference to this problem)
- Allowed additional time in LocaleTest before switching Locale (test was failing intermittently)


---
