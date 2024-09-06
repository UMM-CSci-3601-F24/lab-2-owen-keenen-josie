1. This will make github ignore specific things that we don't need to share!?
2. Gradle is how we run and test things, build.gradle is where the class files are stored and test coverage.
3. Github actions help us do automated testing after we push the things to github for consistency/redundancy!
4. An endpoint is a set of things that a server/client requests specifically for example: http://localserverguy/api/users, in this case api/users is the endpoint.
5. /users is the page where information is gathered, whereas the pages via the API show the actual information in JSON format using the provided information in the endpoint (what type of information, like age, certain Id, etc.)
6. The client folder is the stuff our client is run on, with the different components of the user interface being included as html files. 
7.
  a. It reads the input age from the input element with id "age"
  b. /api/users?&age=(INSERT AGE)
  c. It sends back a sorted Json with all elements with an age attribute equal to the given age
  d. The json from before, it is displayed in a pre with id "jsonDump"
8. lab-2-owen-keenen-josie/client/javascript, the files are todos.js, user.js, util,js. and they are loaded and used in todos.js, users.js, and util.js respectively.
