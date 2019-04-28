rebound
=======

Mock server for isolated functional testing.

Licensed under the Apache 2 license.

[![Build Status](https://travis-ci.org/marcuslange/rebound.svg?branch=develop)](https://travis-ci.org/marcuslange/rebound)

Getting Started
===============

Docker image is available at pileworx/rebound on Docker Hub.

Usage
=====
The server's default port is 8585.
To create a response, PUT to /mock
```json
{
  "method": "PUT",
  "path": "/foo/bar",
  "status": 200,
  "qs": "foo=bar&bar=baz",
  "response": "{\"foo\":\"this is my value\"}",
  "contentType": "application/hal+json"
}
 ```

 A subsequent request to /foo/bar?foo=bar&bar=baz will return an HTTP Status of OK(200), a Content-Type of application/hal+json, and a body of:
 ```json
 {
  "propertyName":"this is my value"
 }
 ```
Properties
==========
 
method
------
 - Required
 
 HTTP Verb. Currently supporting GET, PUT, POST, PATCH, and DELETE. HEAD and OPTIONS are in future plans.
 
path
----
 - Required
 
 URL Path.
 
status
------
 - Required
 
 HTTP Status Code.
 
qs
--
 - Optional
 
 HTTP Query String.
 
response
--------
 - Optional
 
 Stubbed response. The response field supports Velocity VTL for scripting. Velocity VTL documentation can be found here: 
 
 <http://people.apache.org/~henning/velocity/html/ch02s02.html>
 
 example:
 ```json
{
  "method": "POST",
  "path": "/batman/location",
  "status": 201,
  "qs": "foo=bar&bar=baz",
  "response": "[#foreach($i in [1..5]){\"foo\":\"this is my value\"} #if($foreach.count != 5), #end #end]",
  "contentType": "application/hal+json"
}
```
 
contentType
-----------
 - Required
 
 Value of Content-Type header.
 
values
------
 - Optional

Key-Value pairs to use in VTL

example:

```json
{
  "method": "PUT",
  "path": "/foo/bar",
  "status": 200,
  "qs": "foo=bar&bar=baz",
  "response": "{\"foo\":\"$bar\"}",
  "contentType": "application/hal+json",
  "values": {
    "bar": "this is my value"
  }
}
```