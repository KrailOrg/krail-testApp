### Release Notes for krail-testApp 0.7.9

tests for revised Option handling ( davidsowerby/krail#401 ) and use for Event Bus for notifications.  

#### Change log



#### Dependency changes

   compile dependency version changed to: krail:0.9.4
   test compile dependency version changed to: krail-bench:0.7.16
   compile dependency version changed to: krail-jpa:0.8.9

#### Detail

*Version info updated*


---
*Tests for OptionPopup*


---
*Restore to default added to OptionPopup.  This deletes the user level option entry, thus defaulting to next highest ranked entry.*


---
*OptionKey includes default value, OptionPopup introduced*


---
*nullkey removed from default of 18NKey, so some values required*


---
*See [krail 402](https://github.com/davidsowerby/krail/issues/402) UI changed for revised user notifications*


---
*Fluent init calls for DefaultUIModule*

See [krail 374](https://github.com/davidsowerby/krail/issues/374).  Remove the end for sub-classing DefaultUIModule just to set the UI class and application name.  Both can now be done in the Binding Manager by:

 - new DefaultUIModule().applicationTitleKey(key).uiClass(clazz)


---
*Tests modified for [krail 292](https://github.com/davidsowerby/krail/issues/292)*


---
*Grid3x3ViewBase added*

A convenience KrailView class with a 3x3 grid


---
[krail 396](https://github.com/*davidsowerby/krail/issues/396) Private pages visibility*

The issue with BasicForest was causing private pages not to display properly.  Test added to confirm that this is no longer the case


---
