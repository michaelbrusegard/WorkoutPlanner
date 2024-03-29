# About this module

The `restapi` module contains the backend for the app using REST-api. This folder has three classes:

- `WorkoutPlannerServerApp` - initializes the springboot and executes the application
- `ValidateEndpoints` - validator for the endpoints
- `UserController` - the main class for the backend, receives and processes HTTP endpoints

## WorkoutPlannerServerApp

This class is a SpringBoot application, meaning that the configuration for springboot applies.

The main method is what runs springboot using `SpringApplication.run()`, which in turn starts the server.

## ValidateEndpoints

This is a class that provides static methods for validating the endpoints. It is used in the `UserController` class for validation.

### ValidateEndpoints-fields

- (`int`) LIMIT - A limit for what is valid values for the integers in `Exercise`.
- (`int`) MAX_NAME_LENGTH - A limit for the length for an Exercise's name.

### ValidateEndpoints-methods

All the methods are static, and are used to validate the endpoints.

- validateExerciseInput(): Validates all parameters related to adding a new exercise
  - returns true if all conditions are met
- validateExerciseAttribute(): Validates an attribute for a specific `Exercise`

## UserController

This is where all the endpoints are handled. We handle requests by exposing GET, PUT, and DELETE endpoints.

### UserController-fields

- (`User`) user - `User` object from core
- (`Logger`) LOGGER
- (`ObjectMapper`) objectMapper
- (`String`) WORKOUTPLANNER_SERVICE_PATH
- (`boolean`) logObjects

### Overview over endpoints

Here is an overview of the most important endpoints. We chose a lot of GET-endpoints because we only wanted to send the specific values required in a method, rather than storing a core object in the frontend. In hindsight, this is less efficient and not user-friendly if the client has bad internet. A better solution would be to define Data Transfer Objects.

Instead of writing the methods, we choose to look at what happens at the endpoints at each request.

#### GET-endpoints

Get-endpoints retrieves information from the specified endpoint.

- GET /current-workout/exercise-count: Retrieve the count of exercises in the current workout.

- GET /current-workout/exercise/{exerciseIndex}: Retrieve details of a specific exercise in the current workout.
  
- GET /workout/names: Retrieve a list of workout names.
  
- GET /workout/dates: Retrieve a list of workout dates.

#### PUT-requests

PUT-requests send a request to the endpoint to update/modify data.

- PUT /current-workout/{workoutIndex}: Set the current workout based on the provided index.
  
- PUT /current-workout/exercise/{exerciseIndex}: Move an exercise within the current workout.
  
#### DELETE-requests

The DELETE-requests send a request to the delete endpoint to delete data.

- DELETE /workout/{workoutIndex}: Remove a workout based on the provided index.
  
- DELETE /current-workout/exercise/{exerciseIndex}: Remove a specific exercise from the current workout.

### logEndpoint()

There are two `logEndpoint()` methods made for logging information about the HTTP endpoints. They log the endpoint, or the endpoint and its content.
  