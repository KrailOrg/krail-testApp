### Release Notes for krail-testApp 0.8.0

Tests for Krail 0.9.6. 

#### Change log

-   [6](https://github.com/davidsowerby/krail-testApp/issues/6): No test for JPA @Transactional
-   [449](https://github.com/davidsowerby/krail-testApp/issues/449): Invalid issue number or uri.   https://api.github.com/repos/davidsowerby/krail-testApp/issues/449


#### Dependency changes

   compile dependency version changed to: krail:0.9.6
   test compile dependency version changed to: krail-bench:0.8.1
   compile dependency version changed to: krail-jpa:0.9.0

#### Detail

*Pre-release - update version descriptions and version properties*


---
*Vaadin 7.5.3*

see [krail 455](https://github.com/davidsowerby/krail/issues/455)


---
*Fix [449](https://github.com/davidsowerby/krail-testApp/issues/449) Vaadin 7.5.2*


---
*JPaView made scrollable*


---
*DefaultPatternSourceProvider added and tested*


---
*See [krail 432](https://github.com/davidsowerby/krail/issues/432) Support for Export Keys*

DerbyDatabaseWriter added


---
[krail 439](https://github.com/*davidsowerby/krail/issues/439) Vaadin 7.5.1*


---
*refactoring krail-jpa*


---
*SubPagePanelPageObject revised*

The previous method for returning the visible sub-pages fails.  Cause is not certian, but could be the change of theme - the old method relied on the getText() method of the panel.

 Revised to actually look for the navigation buttons


---
*Separated OptionContainer from Pattern*

Data now loads correctly but issue with Jpa method of storing entity (davidsowerby/krail-jpa#12)


---
*OptionView shows active and selected options sources*

Renaming to DefaultOptionSource
OptionKey is no longer immutable, but there is a setter only for the default value.  This is useful for setting a common default for groups of options (see ```SourcePanel``` for an example)


---
*JpaView modified for revised JpaContainerProvider*


---
*Flexible DAO selection [krail 434](https://github.com/(davidsowerby/krail/issues/434) and davidsowerby/krail#435)*

Tests updated
Option selection uses OptionJpaDao in the functional tests


---
[krail 433](https://github.com/*davidsowerby/krail/issues/433) Vaadin 7.5.0*


---
*Trivial parameter name change*


---
*Persistence and JPA  improvements*

Some small changes in Krail core to make Dao structure simpler
Major changes in krail-jpa to provide a single, simplified, transaction-managed generic Dao (```BaseJpaDao```).  krail-jpa also has a simplified API for instance configuration.  This includes removing BlockDao and StatementDao which were not very useful in practice.
Generic Dao can be instantiated for any persistence unit, using annotation identification.
The test-app  adds tests for Dao providers to ensure that bindings are operating correctly


---
*Fix [6](https://github.com/davidsowerby/krail-testApp/issues/6) Test for @Transactional missing*

Additional step to test persistence through @Transactional added


---
[krail 429](https://github.com/*davidsowerby/krail/issues/429)  Fluent methods in I18NModule*

All relevant methods now fluent, so this module could now be fully configured without sub-classing.
Also made naming more consistent - the terms 'source' and 'reader' had been mixed up
Some breaking changes because of the renaming


---
