# shortly-app-android  

#### About
Application is for shortening long urls. It minifies ans stores the links locally so you can copy and use them later. It uses [Shrtcode API](https://app.shrtco.de/docs) for shortening links. 
  
#### Structure
The app was developed by Clean Architecture in a modular structure. It contains Domain, Data, Presentation and App modules.
* Domain - This is an abstraction layer and contains most of contracts
* Data - Implements API calls and application persistence.
* Presentation - Application UI layer 
* App module- Connects all of the modules and hold main configurations


#### Technology stack used:
* Room - to store data locally 
* Coroutines - for asynchronous tasks
* Koin - for dependency injection
* Navigation Component - for navigating through screens
* Retrofit - for API calls
* Timber - for logging
* MockeWebServer - to simulate API calls in unit tests.

#### Unit tests
Unit tests are implemented at the Data layer and cover Room DB calls and Retrofit API calls.

#### To run app
You can use one of the build types to run the application - **prodDebug**, **devDebug**.