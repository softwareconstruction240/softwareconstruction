# cURL

🖥️ [Slides](https://docs.google.com/presentation/d/1pM_tUVD7c6kWpHkEwuRpbWmoBFss3GuK/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

🖥️ [Lecture Videos](#videos)

### 🔑 Key points

- Internet basics: IP addresses, domain names, and port numbers
- Web basics: URLs and the HTTP protocol (headers, methods, and body)
- URL scheme syntax

---

![curl.jpg](curl.jpg)

Curl (technically `cURL`) is an indispensable tool for professional software engineers because it provides a lightweight, protocol-agnostic way to interact with web services directly from the command line. Supporting a vast array of protocols—including HTTP, HTTPS, FTP, and SMTP—cURL allows developers to bypass graphical interfaces to test API endpoints, inspect raw response headers, and debug network connectivity issues with precision.

In professional environments, Curl is frequently used to automate repetitive data transfer tasks within CI/CD pipelines, verify server configurations, and document executable API request examples that can be easily shared across teams. Its ability to be seamlessly integrated into shell scripts makes it a foundational component for backend development, system administration, and automated integration testing.

## Usage

The most basic syntax of curl is to simply provide a URL to the Curl application.

```sh
➜  curl byu.edu

<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">
<html><head>
<title>301 Moved Permanently</title>
</head><body>
<h1>Moved Permanently</h1>
<p>The document has moved <a href="https://www.byu.edu/">here</a>.</p>
</body></html>
```

This will return the HTML page located at the URL. You can see the details of the HTTP connection and request by including the `-v` parameter. This is helpful for debugging HTTP headers and security interactions.

```sh
➜ curl -v byu.edu

* Connected to byu.edu (128.187.16.184) port 80 (#0)
> GET / HTTP/1.1
> Host: byu.edu
> User-Agent: curl/7.87.0
> Accept: */*
>
< HTTP/1.1 301 Moved Permanently
< Date: Thu, 03 Aug 2023 16:52:23 GMT
< Server: Apache/2.5.37 (Red Hat Enterprise Linux) OpenSSL/1.1.1k
< Location: https://www.byu.edu/
< Expires: Thu, 03 Aug 2023 17:52:23 GMT
< Content-Length: 228
< Content-Type: text/html; charset=iso-8859-1
<
<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">
<html><head>
...
```

If you want to make a Curl request that explicitly provides the HTTP method, headers, or body, you can use the following parameters.

| Purpose | Syntax    | Example                            |
| ------- | --------- | ---------------------------------- |
| Method  | -X method | -X PUT                             |
| Header  | -H header | -H "content-type:application/json" |
| Body    | -d body   | -d '{"name":"berners-lee"}'        |

Putting this all together in a single request would look like the following if you were trying to create a new chess game using a server running locally.

```sh
➜ curl -X POST localhost:8080/game -d '{ "gameName": "speed"}' -H "Authorization:607b0857"
```

Take some time to get familiar with Curl. You can use it to test your server, programmatically make batch requests, or to probe and debug how other web services work.

> [!NOTE]
>
> Note that when running under Windows Powershell `curl` is mapped to the `Invoke-WebRequest` command. You don't want to use that. Instead type `curl.exe` to actually access Curl.

## ☑ Exercise


```masteryls
{"id":"dc18f662-992d-4400-be14-87047258c032","title":"Enabling Verbose Mode","type":"multiple-choice"}
When troubleshooting a network request or inspecting the details of a TLS handshake, which flag should you add to a `curl` command to display the full communication details between the client and the server?

- [ ] `-i`
- [ ] `-d`
- [x] `-v`
- [ ] `-L`
```

```masteryls
{"id":"f1ce14d7-1260-41dc-ab21-a4168974c180","title":"Curiosity as a Lifelong Learner","type":"essay"}
How has using tools like curl to explore how systems communicate increased your confidence to tackle unfamiliar technology, and how does that curiosity support BYU's aim of lifelong learning and service?
```


## Videos

- 🎥 [cURL: Client for URLs (9:17)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=16506fca-9d4b-4f5e-89ad-b185015d13e5) - [[transcript]](https://github.com/user-attachments/files/17737197/CS_240_cURL_Client_for_URLs_Transcript.pdf)
- 🎥 [cURL for Chess (4:24)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=07ab3fff-d972-45b1-9f9f-b185015fdf9f) - [[transcript]](https://github.com/user-attachments/files/17737202/CS_240_cURL_for_Chess_Transcript.pdf)
- 🎥 [cURL Alternatives (5:45)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=10dd7fb8-74c3-4b05-a2db-b18501615517) - [[transcript]](https://github.com/user-attachments/files/17737211/CS_240_cURL_Alternatives_Transcript.pdf)
