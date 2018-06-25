# Currencies Exchanger

Simple app for currencies exchanging 


## Technologies, Libraries and Frameworks

 - Language: Kotlin
 - Dagger2 for DI
 - Database ORM: Room
 - RxJava2
 - Mockito for testing
 - ...

## Architecture

The app is designed according to CleanArchitecture principle. There are three layers:

 1. Data - which stores in database or network, and repositories to using this
 2. Domain - contains interactors and models
 3. Presentation (UI layer). This layer is designed according to MVVM architecture pattern. Android Architecture Components (ViewModel, LiveData) are used to implement this pattern.  

Some interactors and ViewModels is covered by unit tests. But the coverage is not 100 percent because of assignment time limits. The test counts can be increased.
