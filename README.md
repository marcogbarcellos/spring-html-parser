# Spring Boot Parser HTML

Simple api that adds a key pair {name: URL}, gets data by name, delete all data and has an endpoint `/annotate` in which given an html string, it will insert hyperlinks in names that already stored in the database(more details below).

I'm using Java(Spring), gradle, docker, and bash/python to run a test script. The database is just a Java Map in memory.

## Instructions to build Project

- Install [Docker](https://docs.docker.com/engine/installation/)
- Install [pip](https://pip.pypa.io/en/stable/installing/)
- After installing pip, Install [html5lib](https://pypi.org/project/html5lib/)
- cd ROOT/OF/PROJECT
- docker build -t html-parser .
- docker run -it -p 8080:8080 html-parser
- sh test.sh

## Instructions

1. Make an HTTP service that satisfies the requirements below. If anything is ambiguous, please first refer to the test script provided. Otherwise, feel free to shoot us an email.
2. Test your service using the script provided (`test.sh`). Start your server and then follow the instructions in the script to run it against your service. We will test your service with a different script and also on tens of thousands of names, so please write code that generalizes and scales.
3. Create a README that contains the following:
   * For each API endpoint, state its space and runtime complexities (Big O) and explain why your implementation achieves these. If you considered alternate implementations, explain why you chose the right one. Be clear and concise (no more than a few sentences per endpoint).
   * Include build instructions that tell us how to compile and run your program. You are welcome to use external libraries in your solution, but make sure they are covered by your build instructions.
4. Create a zip/tar of your solution directory and email to alex.atallah@opensea.io with the subject line, "challenge solution".

## Requirements

Create a web service that annotates HTML snippets by hyperlinking names. Names satisfy the following regex: `[A-Za-z0-9]+`. ("Bob09" is an example name. The string "Alex.com" contains 2 names: "Alex" and "com".)

The service should expose an HTTP API that supports the following operations:

1. Create/update the link for a particular name using an HTTP `PUT` on a URL of the form `/names/[name]`. The body of the request contains JSON of the form `{ "url": "[url goes here]" }`.
2. Fetch the information for a given name using an HTTP `GET` on a URL of the form `/names/[name]`. This should return JSON in the following format: `{ "name": "[name goes here]", "url": "[url goes here]" }`
3. Delete all the data on an HTTP `DELETE` on the URL `/names`. (Note: data is NOT required to persist between server restarts.)
4. The `/annotate` endpoint expects a `POST` request with an HTML snippet (valid HTML on one line) in the request body. It returns the snippet with all occurrences of linkable names hyperlinked with the link stored on the server. If a name occurs in an existing hyperlink, then it is unchanged. No element attributes or tag names should be modified. The returned HTML should not have any new newlines or spaces. You should only annotate complete names that are not part of a larger name. For example, if your server contains the names "Alex" (`http://alex.com`) and "Bo" (`http://bo.com`) and the input snippet is `Alex Alexander <a href="http://foo.com" data-Bo="Bo">Some sentence about Bo</a>`, then the expected output is `<a href="http://alex.com">Alex</a> Alexander <a href="http://foo.com" data-Bo="Bo">Some sentence about Bo</a>`. This endpoint should be robust and work on all valid HTML snippets, so you should use an actual HTML parser (not just a simple regexp find-and-replace).
5. Your implementation should scale to storing tens of thousands of names and annotating snippets that are as long as a typical webpage (e.g., `nytimes.com`). All API endpoints at this scale should run in at most a few seconds.
6. For any endpoint that mutates state, the following contract should hold: after a client receives a response, the change should be reflected in all subsequent API calls. E.g., if I have completed a `PUT` of a new name, I should immediately be able to `GET` it.

## Contents of this directory

- `test.sh` is the test script you can run to test your server.
- `expected_out.txt` is the expected output (used by the test script). You should run the test script in the directory that contains `expected_out.txt`.

## API Endpoints Space and Time Complexities(Big O)

- PUT: O(n) runtime worst case because we can generate the hash, no matter the size of the HashMap but inserting the entry requires checking if the key already exists and this is a potentially linear operation.
- GET: Space and TimeO(n) runtime worst case if there are hashing collisions and all entries end up in the same bucket. We seek an average case of O(1) time to obtain the info for the request. Java HashMaps automatically rebalance the table based on the load factor to achieve this on the fly. `n` is the number of entries in the HashMap (name-URL mappings).
- DELETE: Space and Time O(n) worst case, required to delete all entries in the HashMap.
- POST: O(n) time to annotate the given HTML snippet where `n` is the number of characters in the string. In the worst case, the string consists of names that need to be annotated. We add the relevant link when we encounter a name and then proceed to look through the rest of the string.

I'm using a Java HashMap to store data. So here we have the following complexities: 

- `PUT /names/[name]` . O(n) worst case where n is the number of items in the hashmap Though We will likely achieve an average Time Complexity of O(1) through the help of the hashcode generated by the Java HashMap.
- `GET /names/[name]` . O(n) worst case where n is the number of items in the hashmap. We can achieve an average Time Complexity of O(1) through the hashcode generated by the Java HashMap.
- `DELETE /names` . O(n) worst case where n is the number of items in the hashmap and the hashmap.clear instruction requires the Garbage Collector to remove the 2*N items(Space
- `POST /annotate` . O(NxM) worst case where N is the number of items in the hashmap and M is the number of elements on the Input HTML. We are iterating through the tags(parent -> children -> grandchildren and so on) and looking for the text properties to apply(or not) the hyperlink on users, and for this we iterate through the map making sure all keys will be replaced. A good improvement on this approach would be making it O(n) if we split all the texts of the element by the given regular expression and then look for them in the hashmap(which would have an average time complexity of O(1)) and then replace.
