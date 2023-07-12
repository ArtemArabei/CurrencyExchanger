# Currency Exchanger

## Description

Currency Exchanger is an application for exchanging currencies, written in Kotlin.

Currency rates are provided by the public API and are synchronized every 5 seconds, no authentication is required.
If currency rates or list of currencies are changed, all changes will be applied in the application.

The user has a multi-currency account with zero starting balance of each currency, provided by API, except balance of base currency (EUR by default) - its balance is 1000.
To convert one currency (except base) to another (also except base), user convert first currency to base currency, and then convert it to second currency.
All balances are listed at the top of the screen in the section “My balances” and are sorted by the amount of cash, zero balances are sorted alphabetically.
The user can convert any currency to any if the rate is provided by the API, but the balance can't fall below zero.

To convert currency user have to enter amount, choose a currency to sell and to receive, and press “Submit” button.
The list of currencies to sell contains only those currencies, whose balance is greater than zero, and which the user can actually trade.
After a successful exchange user see a message with all info: currencies to sell and to receive, amounts of this currencies, amount of commission fee.

Commission fee is a highly configurable and flexible tool.
By default the first five currency exchanges are free of charge but after this they changes to 0.7% of the currency being traded.
If needed, all default values (base currency, base balance, commission fee, number of free exchanges, time to update, etc.) can be easily changed in the constants on the ExchangerViewModel.

## Setup

Copy the following link:
https://github.com/ArtemArabei/CurrencyExchanger.git
Open Android Studio and choose "File" -> "New" -> "Project from Version Control".
Paste the link in the "URL:" field and press "Clone ".

## App Architecture

The application consists of two modules - App and Exchanger modules.
App is the entry point to the application, Exchanger is a feature module.
Adding new functionality does not require rewriting the entire application, it is enough to add new feature modules.
Exchanger module, in turn, consists of a date, a domain and a presentation layer.
Also it has DI and Utils packages.
To provide Clean Architecture application uses MVVM design pattern.

## Technologies and Libraries

To provide Dependency injection application uses Dagger2 dependency injection framework.
It also possible to use other libraries, such as Koin, but Dagger2 provides custom scopes, components linking via component dependencies and subcomponents, and it better for system to be maintainable & expandable.
Concurrency in the application is provided by using Kotlin Coroutines, because they are lightweight and very efficient, and make code cleaner and easier to write/read.
RxJava could also be used, but Coroutines is solution for asynchronous programming recommended by Google.
Also App uses Navigation Component, Retrofit2, Lifecycle, etc.

## What can be improved

The ideal is unattainable and there is always a lot of things to be improved.
It is possible to improve design.
Also loading items or Splash Screen can be added.
The code can be shortened, some intermediate classes, variables and functions can be removed.
If task documentation were more detailed, some new features, functions or verifications can be added.

## Contributing 

This application is free to use or modify.

## Maintainers

This project is mantained by:
https://github.com/ArtemArabei
