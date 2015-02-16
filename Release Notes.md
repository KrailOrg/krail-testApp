### Release Notes for krail-testApp 0.7.3

This version adds tests for login navigation. Also adds gitattributes file

#### Change log

-   [2](https://github.com/davidsowerby/krail-testApp/issues/2): Switch to revised I18N annotations


#### Dependency changes

   compile dependency version changed to: krail:0.8.0
   test compile dependency version changed to: krail-bench:0.7.10
   compile dependency version changed to: krail-jpa:0.8.3

#### Detail

*Fix [2](https://github.com/davidsowerby/krail-testApp/issues/2) Switch to revised I18N annotations*

And use the same annotation and key names as Krail core
See [krail 321](https://github.com/davidsowerby/krail/issues/321)


---
*Update version information*


---
*fix [krail 322](https://github.com/davidsowerby/krail/issues/322) Rectified failure after login*

The UserSitemap correctly does not contain the login node in its map after login, but the NavigationRule was attempting to use it.  Provided some utility methods in UserSitemap to assist, isLoginUri(), isLogoutUri etc, and also modified the DefaultNavigator.currentNode() method to check specifically for the login node.

Also added some debug statements
Added equals() methods to Master and UserSitemapNode
Made DefaultMasterSitemap threadsafe (see #244)


---
*gitattributes added*

To overcome Linux/Windows line ending issues


---
