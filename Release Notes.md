### Release Notes for krail-testApp 0.7.6

This version provides changes in support of switch to Docker containers for testing, and in support of fixes made in the Krail core

#### Change log



#### Dependency changes

   compile dependency version changed to: krail-jpa:0.8.6
   compile dependency version changed to: krail:0.9.1
   test compile dependency version changed to: krail-bench:0.7.13

#### Detail

*Version update information*


---
*Change to test for [krail 257](https://github.com/davidsowerby/krail/issues/257)*


---
[krail 353](https://github.com/*davidsowerby/krail/issues/353) Vaadin 7.4.2*

Updated tests.  Had to undo the fix of [152](https://github.com/davidsowerby/krail-testApp/issues/152), as it was causing mock to fail for VaadinSession.  Raised a new ticket [354](https://github.com/davidsowerby/krail-testApp/issues/354).
Also reviewed and eliminated some 'force' statements in the Gradle ResolutionStrategy


---
[krail 260](https://github.com/*davidsowerby/krail/issues/260) Tests set up for Tomcat 8*

Now defaults to Tomcat 8


---
*Changes to support [krail-master 18](https://github.com/davidsowerby/krail-master/issues/18)*


---
*Modify test timing for container*

The push test in a container needed to be slowed down


---
