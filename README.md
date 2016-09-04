# Kaustav-Capcha-API

## Synopsis:
These APIs are used to verify whether a client can count individual distinct words in a given sentence excluding few words.

## API Summary
This API project contains 2 API which a Capcha Client can use.

  1) GET CAPCHA API-> Which serves the client with a Response Body including a sentence and 
  one or more optional excluding words from the sentence. Also the Response header includes
  an attribute "signature".
  
  2) SUBMIT CAPCHA API-> This API is called by the Capcha client to validate a user input. 
  The Client send a request body with the original sentence and the list of excluding words
  received from the API 1 and also a list of individual distinct words and their respective 
  count in the sentence.  The request should also include a header attribute "signature" 
  which was received from the API 1.
  
## Software used
1) Java 1.7

2) Spring 4.3.0

3) Maven has been used to build and package

4) Tomcat 7 for runnning the service

## Build and deploy
If you want to download the source code and build locally and then deploy the service, Please perform the following steps:

1) Download Java (JDK and server JRE)
(link to download http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html)

2) Download Apache Maven 3.3.9 from https://maven.apache.org/download.cgi

Installing instruction: https://maven.apache.org/install.html

Configuring instruction: https://maven.apache.org/configure.html

3) Download and install tomcat from https://tomcat.apache.org/download-80.cgi and set up your tomcat and have it running.

After you have completed the above steps, download the code and use your system command shell to browse to the folder capcha.
From there run the command-> mvn clean install
This will create a WAR file (capcha.war) under target folder within capcha folder.

Tomcat deployment step:

Next, run your tomcat service and access your tomcat manager by going to the following url in your browser:
http://ip:port/manager/html/ 
(ip and port should be the ip of the system where tomcat is running and port where tomcat service is listening)

In the tomcat manager page, go to the section "WAR file to deploy". Click "Choose File" to select the capcha.war in the target 
folder and then click "Deploy". This will deploy the capcha api in the tomcat server and will have it running.

Alternatively, if you already have tomcat 7 or above running, you can download the capcha.war from 
https://www.dropbox.com/s/2b30v4brwq6ldzb/capcha.war?dl=0 to your system and deploy to tomcat using the
Tomcat deployment step mentioned above and select the WAR file from the folder where you have downloaded.

These services are also publicly available at-> http://kaustav-capcha-api.azurewebsites.net

# Accessing the API's

1) GET CAPCHA API - This API can be accessed as below
http://ip:port/capcha/api/getcapcha (to access the public API: http://kaustav-capcha-api.azurewebsites.net/capcha/api/getcapcha)

This API can be called using HTTP GET.

Sample response body returned:

Example 1:

{
  "sentence": "foo",
  "excludes": null
}

Example 2: 

{
  "sentence": "Hodor, hodor hodor. Hodor! Hodor hodor hodor hodor hodor hodor.",
  "excludes": null
}

Example 3:

{
  "sentence": "Call me Kaustav",
  "excludes": [
    "me"
  ]
}

Example 4:

{
  "sentence": "It was the best of times, it was the worst of times, it was the age of 
  wisdom, it was the age of foolishness, it was the epoch of belief, it was the epoch 
  of incredulity, it was the season of Light, it was the season of Darkness, it was 
  the spring of hope, it was the winter of despair, we had everything before us, 
  we had nothing before us, we were all going direct to Heaven, we were all going 
  direct the other way - in short, the period was so far like the present period, 
  that some of its noisiest authorities insisted on its being received, for good or 
  for evil, in the superlative degree of comparison only.",
  "excludes": [
    "it",
    "was",
    "the"
  ]
}

Also every time a response comes back from the API, 
keep a watch on response header where you will see an attribute "signature" 
with a value which needs to be send back in the 2nd API call



1) SUBMIT CAPCHA API - This API can be accessed as below
http://ip:port/capcha/api/submitcapcha (to access the public API: http://kaustav-capcha-api.azurewebsites.net/capcha/api/submitcapcha)

This API can be called using HTTP POST.

While posting a request, just get the "signature" attribute from the response header of the GET CAPCHA API
and then set it in the request header of the SUBMIT CAPCHA API. Also set "Content-Type" as "application/json"
in the request header. Then post a request body similar to given below:

Example 1:

{
  "sentence": "foo",
  "excludes": null,
  "wordCount": [
    {
      "word": "foo",
      "count": 1
    }
  ]
}

Example 2:

{
  "sentence": "Hodor, hodor hodor. Hodor! Hodor hodor hodor hodor hodor hodor.",
  "excludes": null,
  "wordCount": [
    {
      "word": "Hodor",
      "count": 10
    }
  ]
}

Example 3:

{
  "sentence": "Call me Kaustav",
  "excludes": [
    "me"
  ],
  "wordCount": [
    {
      "word": "Call",
      "count": 1
    },
    {
      "word": "Kaustav",
      "count": 1
    }
  ]
}

Example 4:

{
  "sentence": "The quick brown fox jumped over the lazy dog.",
  "excludes": [
    "the",
    "dog"
  ],
  "wordCount": [
    {
      "word": "brown",
      "count": 1
    },
    {
      "word": "fox",
      "count": 1
    },
    {
      "word": "jumped",
      "count": 1
    },
    {
      "word": "lazy",
      "count": 1
    },
    {
      "word": "over",
      "count": 1
    },
    {
      "word": "quick",
      "count": 1
    }
  ]
}

You can use any API testing software to test these APIs. I used a Chrome REST Client App ARC.

## Code explanation

Overall summary:

There is a text file capcha-sentences.txt

It is expected to store individual sentences in this text file and if you want to have certain word
to be excluded, put those words as "|" delimited on the same line of the sentence. Any sentence
which has just one "distinct" word should not have any excludes. This check is not there in the code
but an assumption is being made that the text file should be created with the above rule in place.
As an improvement the API can download this text from another API and can have an admin application
to create this data and impose any restrictions needed.

GET CAPCHA API

The first Get Capcha API call loads this data on the from the text file and cache it in a Singleton class
so that the subsequent GET CAPCHA call can use this from the cache.
After loading the data, it counts how many lines are there in the file.
Then it generates a random number between 1 to the length of the files (both 1 and the length is inclusive)
and then select that specific line. Then parse the line using "|" delimiter to see if there any excluding
words. Then create a Java POJO with the sentence and the excluding words. 
It also concat the sentence and all the excluding words together and generate a hex string which is send in
the signature attribute. For example if the sentence is "Call me Kaustav" and the excluding word is "me", then
it will create a concat string of "Call me Kaustavme" and generate the hex string. Another example, if the
sentence is "The quick brown fox jumped over the lazy dog." and the excluding words are "the" and "dog" then
the concat string will be "The quick brown fox jumped over the lazy dog.thedog" and generate the hex string.

SUBMIT CAPCHA API

The Submit Capcha API first perform certain validations:
i) Whether the request include a header attribute "signature" or not
ii) Whether the request body includes "sentence" or not
iii) Whether the request body includes one or many "wordCount" or not

If any of the above validation fails, it returns a HTTP status code 400 with specific message

If the above set of validation passes, it generates a hex string using the "signature" and "excludes" words
using the same logic which was done in the GET CAPCHA API. If this generated hex string doesn't match with 
the value in the "signature" attribute, then the API return a HTTP status code 400 with specific message.

If above signature validation passes, then the api takes the sentence, then strip of all punctuations and the
excluding words (case insensitive) from the sentence and create a map of distinct words and its count.
Then from the request body wordCount, it also generates a map of distinct words and it's count.

Next the API validates where these 2 maps are of same size and the mapping in those 2 maps are same or not.
If it is of same size and the mappings are same, then it returns a HTTP status code of 200 with a success message
else returns a HTTP status code of 400 with specific message.

## Signature Attribute

This attribute has been introduced to make sure that the capcha answer received in the SUBMIT CAPCHA API
is related to the correct and specific capcha question which was given earlier. This is to safe guard that
the client is not answering a "Correct" answer for a different question which was not the question asked
to the client.

## Individual Code explanation

1) Resource -> capcha-sentences.txt to store the sentences and the excluding words

2) pom.xml for the maven build.

3) CapchaConfiguration.java and CapchaInitializer.java is used to set up the Spring configuration on load and also to 
set up a context path "/" to the Spring Dispatcher servlet. The configuration also scan any files or subfolders for
Spring annotated classes and sets up the REST APIs on startup.

4) CapchaController.java - This is the Controller class which host 2 RESTful API's. This request controller class is
invoked for a path "/api" and the individual apis are accessed by "/api/getcapcha" and "/api/submitcapcha".
When GET CAPCHA API is invoked, the request is handled by the method: 

@RequestMapping(value="/getcapcha",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<CapchaData> getCapcha(HttpServletResponse response)

When the SUBMIT CAPCHA API is invoked, the request is handled by the method:

@RequestMapping(value="/submitcapcha",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<String> submitCapcha(@RequestBody CapchaDataWithCount data, HttpServletRequest request)

5) All the classes under com.kaustav.capcha.pojo are POJO classes used to generate JSON response body and map JSON request body.

6) CapchaSentenceMaster.java class is used to load the sentences and excluding words from the text file, generate a random number
to select a particular sentence and its excluding words and serve the "/getcapcha" api to serve the capcha question to the client.

7) WordCountValidator.java class is used to serve the SUBMIT CAPCHA API once preliminary validations are passed in the CapchaController.java
The WordCountValidator.java then strip the punctuations and the excluding words from the sentence in the request body, finds the
distinct words and its respective count in the modified sentence. It then compares that with the "wordCount" passed in the request
body.

8) SignatureUtil.java is used to generate a hex string out of a sentence and its excluding words.
