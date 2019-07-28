rebound
=======

Mock server for isolated functional testing.

Licensed under the Apache 2 license.

[![Build Status](https://travis-ci.org/pileworx/rebound.svg?branch=develop)](https://travis-ci.org/pileworx/rebound)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/fe73be17d3544f06b27911470214e3e6)](https://www.codacy.com/app/marcuslange/rebound?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=pileworx/rebound&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/fe73be17d3544f06b27911470214e3e6)](https://www.codacy.com/app/marcuslange/rebound?utm_source=github.com&utm_medium=referral&utm_content=pileworx/rebound&utm_campaign=Badge_Coverage)

Getting Started
---------------

Docker image is available at pileworx/rebound on Docker Hub.

Usage
-----
The server's default port is 8585.
To create a response, PUT to /mock
```json
{
  "scenario": "Scenario Name",
  "when": {
    "request": {
      "method": "PUT",
      "path": "/foo/bar",
      "query": "foo=bar&bar=baz",
      "headers": [
        {
          "name": "accept",
          "value": "application/json"
        }
      ],
      "body": "{\"foo\":\"bar\"}"
    }
  },
  "then": [
    {
      "status": 200,
      "headers": [
        {
          "name": "content-type",
          "value": "application/hal+json"
        }
      ],
      "body": "{\"foo\":\"$bar\"}",
      "values": {
        "bar": "this is my first value"
      }
    }
  ]
}
 ```

A subsequent request to /foo/bar?foo=bar&bar=baz will return an HTTP Status of OK(200), a Content-Type of application/hal+json, and a body of:
 ```json
{
  "propertyName":"this is my value"
}
 ```
Properties
----------

scenario
--------
-   Required

Name of the Scenario you are creating

when
------
-   Required

when.request
------------
-   Required

The http request to expect

when.request.method
-------------------
-   Required
 
HTTP Verb. Currently supporting GET, PUT, POST, PATCH, and DELETE. HEAD and OPTIONS are in future plans.
 
when.request.path
-----------------
-   Required
 
URL Path.
  
when.request.query
------------------
-   Optional
 
HTTP Query String.

when.request.headers
--------------------
-   Optional
 
A list of name value pairs of the request headers.

when.request.body
--------------------
-   Optional
 
Expected body of POST, PUT, or PATCH requests.

then
----
- Required

A list of expected responses.

Responses will be returned in order submitted and will return a failed response once the list is exhausted. 
If one response is submitted. It will return that response on every matching request without a failure.

then.[n].status
------
-   Required
 
HTTP Status Code.

then.[n].headers
--------------------
-   Optional
 
A list of name value pairs of the response headers.
 
then.[n].body
--------
-   Optional
 
Stubbed response body. The body field supports Velocity VTL for scripting. Velocity VTL documentation can be found here: 
 
<http://people.apache.org/~henning/velocity/html/ch02s02.html>
 
example:
 ```json
{
  "status": 200,
  "headers": [
    {
      "name": "content-type",
      "value": "application/hal+json"
    }
  ],
  "body": "[#foreach($i in [1..5]){\"foo\":\"this is my value\"} #if($foreach.count != 5), #end #end]"
}
```
 
then.[n].values
------
-   Optional

Key-Value pairs to use in VTL

example response:

```json
{
  "status": 200,
  "headers": [
    {
      "name": "content-type",
      "value": "application/hal+json"
    }
  ],
  "body": "{\"foo\":\"$bar\"}",
  "values": {
    "bar": "this is my first value"
  }
}
```