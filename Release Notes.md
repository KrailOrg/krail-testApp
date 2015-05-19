### Release Notes for krail-testApp 0.7.8

Upgrade to Vaadin 7.4.6, Guice 4.0.  Minor change to build file for bintrayUpload.  Changes to reflect different I18N processing

#### Change log



#### Dependency changes

   compile dependency version changed to: krail:0.9.3
   test compile dependency version changed to: krail-bench:0.7.15
   compile dependency version changed to: krail-jpa:0.8.8

#### Detail

*Updated version info*


---
*Vaadin 7.4.6*


---
*Vaadin 7.4.5*


---
*Drilldown removed from @Caption etc, as it is no longer used by the I18NProcessor*


---
*Modified views as a result of [krail 283](https://github.com/davidsowerby/krail/issues/283)*


---
*Updated to reflect simplification of application setup, see [krail 374](https://github.com/davidsowerby/krail/issues/374)*


---
*UI modified to include Option*


---
[krail 371](https://github.com/*davidsowerby/krail/issues/371) ViewBase implements KrailView.beforeBuild()*

Empty implementations removed


---
*See [krail 374](https://github.com/davidsowerby/krail/issues/374) UIModule provides defaults*

UIModule was previously abstract and had to be sub-classed for every application.  It has been renamed DefaultUIModule, and uses DefaultApplicationUI to remove the need for sub-classing.  This application updated for this change.


---
