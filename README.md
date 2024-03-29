# Workout Planner

This is the repository for group gr2307 in the course IT1901 at NTNU. The project is an app to create workout plans made using maven with a javafx frontend and spring boot rest backend.

### Video example

https://github.com/michaelbrusegard/Informatics-Project-1/assets/56915010/10246231-a0d0-44d8-a367-c1e20b4fbf67

### Note

Ignore any references to eclipse che and the GitLab CI. These were tools only used for the university course, the app can run without them. The GitLab CI does not transfer to GitHub.

You can find my personal refleksjonsrapport [here](Refleksjonsrapport.pdf). This is a reflection about code quality in the project, because I was responsible for writing most of the code in the project.

## Versions

maven: 3.8.3 \
java: openjdk-17 \
javafx: 17.0.8

## Running and testing the app

To run the app, first enter the eclipse che workspace with the following link:

[Open in Eclipse Che](https://che.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2307/gr2307.git?new)

You can either run the app locally or with the REST-server.

To run it locally, you can change the value of `useRemote` in `MainController` from true to false. This makes it so that the app runs without the REST-server and accesses the core logic directly.

Then navigate to the source code directory of the project, and clean install without tests:

```bash
cd workoutplanner
mvn clean install -DskipTests
```

This installs the necessary dependencies for the app to work. If you want to run the tests when running clean install, you need to make sure that `useRemote` is set to false. This is because the tests are run with the REST-server when `useRemote` is true, and if you don't have the server running, the tests will fail.

If you have the server running, you can run the tests with the following command:

```bash
mvn test
```

To run the REST-server, from the `workoutplanner` folder navigate to `restapi` by running:

```bash
cd restapi
```

followed by:

```bash
mvn spring-boot:run
```

to run the server.

To then start the javafx app, open a new terminal and navigate to the `workoutplanner` folder, then navigate to `fxui`:

```bash
cd fxui
```

and run the command:

```bash
mvn javafx:run
```

The app should now be up and running.

In Eclipse Che, you can go to the 'Endpoints' window and copy the link from port 6080. This will take you to the virtual machine, where you can view the app.

### Run app with jlink and jpackage

To do this, you also need to navigate to the `fxui` folder in `workoutplanner`. Then you need two commands:

```bash
mvn javafx:jlink
mvn jpackage:jpackage
```

Make sure you have changed the value of `useRemote`, or have started the server.
If you get an error for not having a toolset to create executables, you need to download this first.
Then you open the `target` folder in `fxui`, where there should be a `dist` folder. If you run the file in the `dist` folder,
you should get the installer for the app.
Then download the app to your desired destination, and run the program.

In Eclipse Che, you can go to the 'Endpoints' window and copy the link from port 6080. This will take you to the virtual machine, where you can view the app.

## Run JaCoCo

To run JaCoCo, you need to navigate to the corresponding module (core or fxui) and then run the following command in the module directory:

```bash
mvn jacoco:report
```

It creates the site-folder in the target-folder of the corresponding module, where if you open target/site/jacoco/index.html you will get the code coverage report.

Alternatively, you will also find the code coverage report for the whole project in the target folder of the aggregate module if you ran the tests with clean install.

## Contents

- ./workoutplanner - [source code](./workoutplanner)
  - ./core - [core logic](./workoutplanner/core)
  - ./fxui - [javafx user interface](./workoutplanner/fxui)
  - ./fxutil - [javafx utility classes](./workoutplanner/fxutil)
  - ./restapi - [rest api server](./workoutplanner/restapi)
- ./docs - [assignment documentation](./docs)
- ./CHANGELOG.md - [changelog](./changelog)

In the root directory, you can find the documentation for the assignments in the `docs` folder and the CHANGELOG.md file. The app is located in the `workoutplanner` folder, where you can find the source code for the app. This src-directory also contains a markdown for user-stories, diagrams, and a readme about the app. The `core` folder contains the core logic for the app, as well as persistence. The `fxui` folder contains the javafx user interface and the `fxutil` folder contains utility classes for the javafx user interface. The `restapi` folder contains the backend of the app, this is where the REST-api is.

### Link to project documentation files

- [README](workoutplanner/README.md) - Contains information about the project and concept photos of the app.
- [USER_STORIES](./docs/USER_STORIES.md) - Contains the user stories for the project
- [DIAGRAMS](./docs/UML) - Contains class-diagram, sequence-diagram and package-diagram
- [work flow, work habits and code quality](./docs/DEVELOPMENT.md) - Contains documentation for the persistence of the program.
- [releases](./docs/releases) - Contains the different releases for each milestone/assignment.
- [challenges](./docs/CHALLENGES) - Contains a challenge we had with eclipse.

- [core documentation](workoutplanner/core/CORE.md)
- [persistence](workoutplanner/core/CORE.md#persistence)
- [fxui documentation](workoutplanner/fxui/FXUI.md)
- [fxutil documentation](workoutplanner/fxutil/FXUTIL.md)
- [rest documentation](workoutplanner/restapi/RESTAPI.md)
