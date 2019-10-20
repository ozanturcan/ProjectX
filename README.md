# Omdb Project - This project forked from  [ProjectX](https://github.com/muratcanbur/ProjectX)


Android App using The Online Movie DB API


## Description

a simple app that contains some basic functionality and it's made for code case interview

## Tech Stack
- Dagger 2 - Used to provide dependency injection
- Retrofit 2 - OkHttp3 - request/response API
- Glide - for image loading.
- RxJava 2 - reactive programming paradigm
- LiveData - use LiveData to see UI update with data changes.
- Data Binding - bind UI components in layouts to data sources
- Firebase Remote Config - for getting new config without update version

## Overview of app arch.
- follow the rules from Architecture guidelines recommended by Google.
- keep Activity only responsible for UI related code 
- ViewModel provides data required by the UI class
- Repository layer provides data to ViewModel classes. (single source of truth)
