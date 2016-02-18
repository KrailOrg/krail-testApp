### Release Notes for krail-testApp 0.8.3

Tests for Krail 0.9.9.  Vaadin 7.6.3.  Separated from master project

#### Change log

-   [09](https://github.com/davidsowerby/krail-testapp/issues/9): Separate from master project
-   [10](https://github.com/davidsowerby/krail-testapp/issues/9) Push failure*


#### Dependency changes

   compile dependency version changed to: krail:0.9.9
   test compile dependency version changed to: krail-bench:0.8.4
   compile dependency version changed to: krail-jpa:0.9.3

#### Detail

*Release notes and version.properties generated*


---
*Version files updated*


---
*See [9](https://github.com/davidsowerby/krail-testapp/issues/9) Separate from master project*

All steps complete

---
*See [10](https://github.com/davidsowerby/krail-testapp/issues/10) Push failure*

The renaming of the war file has been removed from build.gradle, including it caused errors.  File is now renamed as part of Dockerfile.
Subsequently realised that war renaming is fine, just needs IDEA to use the exploded war, or to set up build paths for exploded/standard war correctly in IDEA

UITestServlet removed, it was causing an Atmosphere warnings about duplicate Endpoints

@Ignore two intermittent test failures, see #11 and #15


