# Hypertext Transfer Protocol

🖥️ [Slides](https://docs.google.com/presentation/d/1VECGwiLgXd541yq9BWVSYiphHAXHG0ca/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

🖥️ [Lecture Videos](#videos)

📖 **Optional Reading**: [MDN An overview of HTTP](https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview)

### 🔑 Key points

- Internet basics: IP addresses, domain names, and port numbers
- Web basics: URLs and the HTTP protocol (headers, methods, and body)
- URL scheme syntax


![Tim Berners Lee](timBernersLee.jpg)

> “You affect the world by what you browse”
>
> — Tim Berners-Lee, (**Source**: _Answers for Young People_)
---

## URL

The Uniform Resource Locator (URL) represents the location of a web resource. A web resource can be anything: a web page, font, image, video stream, database record, or JSON object. It can also be ephemeral, such as a visitation counter or a gaming session.

Analyzing the parts of a URL is a good way to understand how resources are identified. Here is an example URL representing a query to BYU for cities containing "pro" using secure HTTP (HTTPS).

![urlSchema.jpg](urlSchema.jpg)

The URL syntax follows this convention. Note the punctuation used to delimit the various parts. Most parts of the URL are optional; the only required elements are the scheme and the domain name.

```yaml
<scheme>://<domain name>:<port>/<path>?<query string>#<fragment>
```

| Part        | Example                              | Meaning                                                                                                                                                                                                                                                                             |
| ----------- | ------------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Scheme      | https                                | The protocol required to request the resource. For web applications, this is usually HTTPS. It could also be other protocols like FTP or MAILTO.                                                                                                                                    |
| Domain name | byu.edu                              | The domain name that owns the resource represented by the URL.                                                                                                                                                                                                                      |
| Port        | 3000                                 | The numbered network port used to connect to the server. Lower-numbered ports are reserved for common internet protocols (e.g., 80 for HTTP, 443 for HTTPS). Higher numbers can be used for custom services.                                                                        |
| Path        | /school/byu/user/8014                | The path to the resource on the server. This does not have to match a physical file system; it can be a logical path representing API endpoints, database records, or object schemas.                                                                                               |
| Query string  | filter=names&highlight=intro,summary | A list of key-value pairs, also called the **query string**. These provide additional qualifiers, such as filters or formatting instructions for the requested resource.                                                                                                           |
| Fragment      | summary                              | Also called a **hash** or **anchor**, this represents a sub-location within the resource. For HTML, the browser automatically scrolls to the element with a matching ID. Note: Anchors are handled by the browser and are not typically sent to the server.                   |

Technically, you can also provide a username and password before the domain name (e.g., `https://user:pass@example.com`). This was used historically for authentication but is now deprecated for security reasons. However, you will still see this convention in database connection strings.

## HTTP

Hypertext Transfer Protocol (HTTP) is the language of the web. When a browser makes a request to a server, it uses HTTP. Understanding the high-level internals of HTTP is essential for web development; just as fluency in a language helps you navigate a foreign country, speaking HTTP helps you communicate effectively with web services.

When a web client (the browser) and a web server communicate, they exchange **requests** and **responses**. You can inspect these exchanges using browser developer tools or command-line tools like `curl`.

For example, you can use `curl` in your terminal to view the raw HTTP exchange:

```sh
curl -v -s http://info.cern.ch/hypertext/WWW/Helping.html
```

### Request

An HTTP request for the command above looks like this:

```http
GET /hypertext/WWW/Helping.html HTTP/1.1
Host: info.cern.ch
Accept: text/html
```

The general syntax of an HTTP request is:

```yaml
<verb> <path and parameters> <version>
[<header key: value>]*
[

  <body>
]
```

The first line (the **request line**) contains the `verb` (the action), followed by the path and parameters, and finally the HTTP version. The subsequent lines are optional **headers** defined as key-value pairs. After the headers, there is an optional **body**, separated from the headers by a blank line.

In the example above, we use the `GET` verb to request the resource at `/hypertext/WWW/Helping.html`. The `Host` header specifies the domain, and the `Accept` header tells the server that the client expects `text/html` (a [MIME type](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types)).

### Response

The server's response to the request looks like this:

```yaml
HTTP/1.1 200 OK
Date: Tue, 06 Dec 2022 21:54:42 GMT
Server: Apache
Last-Modified: Thu, 29 Oct 1992 11:15:20 GMT
ETag: "5f0-28f29422b8200"
Accept-Ranges: bytes
Content-Length: 1520
Connection: close
Content-Type: text/html

<TITLE>Helping -- /WWW</TITLE>
<NEXTID 7>
<H1>How can I help?</H1>There are lots of ways you can help if you are interested in seeing
the <A NAME=4 HREF=TheProject.html>web</A> grow and be even more useful...
```

The general syntax of an HTTP response is:

```yaml
<version> <status code> <status string>
[<header key: value>]*
[

  <body>
]
```

The response syntax mirrors the request syntax. The primary difference is the first line (the **status line**), which contains the HTTP version, a numeric status code, and a descriptive status message.

## Verbs (Methods)

HTTP verbs describe the action the requester wants to perform.

| Verb    | Meaning                                                                                                                                                                                                                                                  |
| ------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| GET     | Retrieve the requested resource. This can be a single item or a list of resources.                                                                                                                                                                       |
| POST    | Create a new resource. The request body contains the data for the new resource. The response usually includes the unique ID of the created item.                                                                                                         |
| PUT     | Update a resource. The URL or body must contain the unique ID of the resource. The request body contains the updated data.                                                                                                                               |
| DELETE  | Remove a resource. The URL or headers must specify the unique ID of the resource to be deleted.                                                                                                                                                          |
| OPTIONS | Retrieve metadata about the communication options for a resource. Usually, only headers are returned without a body.                                                                                                                                     |

## Status Codes

Standard HTTP status codes allow the client to understand the result of the request. Codes are grouped into five classes:

- **1xx (Informational):** The request was received, and the process is continuing.
- **2xx (Success):** The action was successfully received, understood, and accepted.
- **3xx (Redirection):** Further action is needed to complete the request.
- **4xx (Client Error):** The request contains bad syntax or cannot be fulfilled.
- **5xx (Server Error):** The server failed to fulfill an apparently valid request.

Common codes include:

| Code | Text                  | Meaning                                                                                                                           |
| ---- | --------------------- | --------------------------------------------------------------------------------------------------------------------------------- |
| 200  | OK                    | The request succeeded, and the resource is returned.                                                                              |
| 201  | Created               | The request succeeded and a new resource was created.                                                                             |
| 204  | No Content            | The request succeeded, but there is no content to send back (common for DELETE).                                                  |
| 304  | Not Modified          | The cached version of the resource is still valid; no need to re-transfer data.                                                   |
| 307  | Temporary Redirect    | The resource is temporarily at a different URL.                                                                                   |
| 308  | Permanent Redirect    | The resource has been moved permanently to a new URL.                                                                             |
| 400  | Bad Request           | The server cannot process the request due to a client error (e.g., malformed syntax).                                             |
| 401  | Unauthorized          | The request lacks valid authentication credentials.                                                                               |
| 403  | Forbidden             | The server understands the request but refuses to authorize it.                                                                   |
| 404  | Not Found             | The server cannot find the requested resource.                                                                                    |
| 429  | Too Many Requests     | The client has sent too many requests in a given amount of time (rate limiting).                                                  |
| 500  | Internal Server Error | A generic error message when the server encounters an unexpected condition.                                                       |
| 503  | Service Unavailable   | The server is currently unable to handle the request (due to maintenance or overload). Clients should try again later.            |

## Headers

📖 **Optional Reading**: [MDN HTTP headers](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers)

Headers provide metadata about the request or response, such as security tokens, data formats, and caching instructions.

| Header                      | Example                              | Meaning                                                                                             |
| --------------------------- | ------------------------------------ | --------------------------------------------------------------------------------------------------- |
| Authorization               | Bearer bGciOiJIUzI1NiIsI             | Credentials for authenticating the client.                                                          |
| Accept                      | application/json                     | The content formats the client is willing to receive.                                               |
| Content-Type                | text/html; charset=utf-8             | The format of the data in the body of the message.                                                  |
| Cookie                      | SessionID=39s8cgj34                  | Key-value pairs previously sent by the server and stored by the client.                             |
| Host                        | info.cern.ch                         | The domain name of the server (required in HTTP/1.1).                                               |
| Origin                      | https://cs260.click                  | Indicates where the request originated from (used for security).                                    |
| Access-Control-Allow-Origin | *                                    | A response header indicating which origins are allowed to access the resource.                      |
| Content-Length              | 368                                  | The size of the body in bytes.                                                                      |
| Cache-Control               | public, max-age=604800               | Instructions for how the response should be cached by the browser or proxies.                       |
| User-Agent                  | Mozilla/5.0 (Macintosh)...           | Information about the client software making the request.                                           |

## Body

The body contains the actual data being transferred. Its format is defined by the `Content-Type` header. Common formats include HTML (`text/html`), images (`image/png`), JSON (`application/json`), and JavaScript (`text/javascript`). While `GET` requests typically do not have a body, `POST` and `PUT` requests use the body to send data to the server.

## ☑ Exercise


```masteryls
{"id":"f4e03e17-bc29-4e17-b48e-45c138c3abfb","title":"Identifying URL Components","type":"multiple-choice"}
In a standard URL (Uniform Resource Locator), which segment is used to pass data to the server as a series of key-value pairs, typically following the resource path and starting with a question mark (`?`)?

- [ ] The fragment
- [x] The query string
- [ ] The scheme
- [ ] The hostname
```

```masteryls
{"id":"569eaf90-5ac2-46a1-a115-fe54bb1685ad", "title":"Essay", "type":"essay" }
Describe the different parts of an HTTP request.
```


## Videos

- 🎥 [Chess Server Review (3:39)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=a3d5b141-7756-4b0f-8912-b185014dbd5e) - [[transcript]](https://github.com/user-attachments/files/17738495/CS_240_Chess_Server_Review_Transcript.pdf)
- 🎥 [HTTP Overview (10:42)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ef2d29d2-72af-4d8f-9cdc-b185014f4fe5) - [[transcript]](https://github.com/user-attachments/files/17738497/CS_240_HTTP_Overview_Transcript.pdf)
- 🎥 [HTTP GET Requests (23:29)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ea7a075b-a516-40f2-b17b-b185015286d8) - [[transcript]](https://github.com/user-attachments/files/17738504/CS_240_HTTP_GET_Requests_Transcript.pdf)
- 🎥 [HTTP POST Requests (8:43)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=90dad290-87d6-44c5-9aa5-b185015930a1) - [[transcript]](https://github.com/user-attachments/files/17738509/CS_240_HTTP_POST_Requests_Transcript.pdf)
- 🎥 [HTTP Methods (2:49)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=50ec71e4-a0d9-4f55-853e-b185015bd652) - [[transcript]](https://github.com/user-attachments/files/17738511/CS_240_HTTP_Methods_Transcript.pdf)