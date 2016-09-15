# RenderingImage
This system was developed following the premises below:
```console
As a user of the movingimage platform, I want to have still images generated from my videos and available for display in my web portal.

Acceptance criteria:

• A still image (JPEG format) can be rendered from any video available from the VideoManager application.
• The image will show a still of the video at a specified time-stamp (offset).
• The video-id of the VideoManager application is used to identify the video.
• The image can be accessed via a URL.
• Rendering of the image can be initiated through a HTTP interface.
• The video-id and time-stamp can be specified when initiating the rendering of the image.
• The still image is available in a number of dimensions as appropriate.
```

## Technologies
At this section I'll list and explain which technologies I've chosen to work on:
* Java - the required language for this application
* RestEasy - Lightweight framework to create REST APIs [http://resteasy.jboss.org/]
* Google Guice - This library manages object creation and instantiation and it's very lightweight in order to avoid heavy footprints during bootstrap application [https://github.com/google/guice]
* GSON - This library was used to convert API's response endpoint into objects from the system. Again, it's very simple and lightweight [https://github.com/google/gson]
* Google Guava - This library has a lot of tools to help creating clean and simple code. I've used a cache in-memory library to store accessToken in order to avoid calling Credential API each time [https://github.com/google/guava]
* Http Components - This library helps on HTTP requests/responses and is pretty simple to set timeouts, retries and other directives [https://hc.apache.org/]
* Logback - Async logging library [http://logback.qos.ch/]
* JUnit - unit tests framework to test each class and method individually [http://junit.org/junit4/]
* Mockito - Framework to create objects mocks helping unit tests to segregate layers and objects dependencies [http://mockito.org/]

## Motivations and Business Rules
The system was designed to be simple but at the same time using approaches to help easy understanding, maintain and improve the system like some other production should have. Below there's a list of mindsets that guided implementation:
* Rest API - I've created a simple REST API with videoId and timestamp as PathParam and a optional QueryParam called dimension. This queryParam is to filter some still image providing height dimension. If dimension provided doesn't exist for the still image, a thumbnail will be rendered. If no dimension was provided, all dimension images will be rendered for the provided timestamp.
* Caching - to avoid calling credential API every time, I've put accessToken on a in-memory cache with same expiration as the key, i.e 5 minutes.
* Logging - every production application must have logging for troubleshooting and to know used parameters on each flow and so on.
* Exceptions handling - the code is supported by an exception mechanism controlling the flow. This approach needs more code (creating custom exceptions and check all exceptions) but lets the system more robust.
* IoC - Using IoC (Inversion of Control) mechanism helps to not creating a lot of objects in memory and avoiding large footprints besides code gets simpler as well. 
* Unit tests - To make sure all the corner cases were coverage including code coverage. The application has 11 unit tests at this moment covering DAOs and Service layers ;)

## How to run
The system produces a war file and can be easily started by calling embedded jetty into maven, so to run you just need Maven and Java installed on machine. After setting up them, just run the following command:
> `mvn clean install jetty:run`

```console
[INFO] Started ServerConnector@36570936{HTTP/1.1}{0.0.0.0:8080}
[INFO] Started Jetty Server
```
When the lines above appear in the console the application is up and running to start receiving requests

## Requests Examples
* http://localhost:8080/renderingimage/image/DoVg8GbZoWyPp72YmaaFmz/2766?dimension=288 

In the example above, the provided parameters were:
```console
videoId = DoVg8GbZoWyPp72YmaaFmz
timestamp = 2766
dimension = 288
```

In this case, system will return the still image with dimension 288p of height only.

* http://localhost:8080/renderingimage/image/DoVg8GbZoWyPp72YmaaFmz/2766

In this case the system will return all still images dimension for the informed timestamp and videoId.

* http://localhost:8080/renderingimage/image/DoVg8GbZoWyPp72YmaaFmz/2766?dimension=12345

In this case the dimension provided is invalid, so the system will return thumbnail image for the still image only.

## Alternative flows
Currently system is designed to validate the following corner cases:
* Invalid Timestamp = the system will return HTTP Status Code 400 and a message informing this situation
* Invalid Video Id = the system will return HTTP Status Code 404 and a message informing this situation
* Credential error = the system will return HTTP Status Code 500 and a message informing this situation
* Error on getting Video data = the system will return HTTP Status Code 500 and a message informing this situation
* Error on getting image bytes = the system will return HTTP Status Code 500 and a message informing this situation