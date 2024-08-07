# GitHub user search engine
Application for searching github user repositories.

## Table of Contents
* [Introduction](#introduction)
* [Features](#features)
* [Technologies](#technologies)
* [Setup](#setup)

## Introduction
A simple application for searching user repositories on Github.
Thanks to this application, we can easily search for all user repositories that are not forks.
We get the name of the repository, who is the owner,
the name of the branch and the last commit saved in sha.
## Features

*  Search user repositories

## Technologies
* Spring Boot 3.3.2
* Gradle
* Mockito
* Wiremock
* JUnit

## Setup
To get started with this project, you will need to have the fallowing installed on your local machine:
* JDK 21
* Gradle 8.5

To build and run project, fallow these steps:
* Clone the repository: 'git clone https://github.com/PawelKowalskiSD/github-user-search-engine'
* Navigate to the project directory: cd github-user-search-engine
* Build the project: gradle build
* Run the project: ./gradlew bootRun

The application will be available at: http://localhost:8080/v1/user/{Username}