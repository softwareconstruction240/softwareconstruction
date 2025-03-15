# PetShop Server Sequence Diagram

> [!TIP]
> View the [sequence diagram][sequence-diagram-link] directly.

Sequence diagrams can be used for many things. In this context, a sequence diagram shows the logical flow of a program as it executes. It visualizes the function calls, the parameters, and return types of the most important functions. As with any diagram, minor details are sometimes omitted to avoid distracting from the overall purpose of the diagram.

The diagram is designed to illustrate **code**; each element on the sequence diagram corresponds to something that will be literally implemented in the source code. The PetShop example is intentionally simple in its implementation in order to emphasize core patterns and principles which underlie real-world software applications.

This sequence diagram shows each of the public PetShop HTTP endpoints, and the internal organization of the PetServer that responds to each request.

## Layer Overview

An emphasis is placed on the separation of responsibility between multiple layers. Each layer has a primary responsibility and solves a specific portion of the complexity of the application. This system follows strict [N-1 dependency](https://www.oreilly.com/library/view/software-architecture-patterns/9781491971437/ch01.html) rules: each layer only interacts with layers _directly_ beneath it, and does not access layers arbitrarily deep in the hierarchy.

| Layer | Implemented By | Description |
| :---: | :------------- | :---------- |
| **Client** | Not necessarily a human, but the direct user of this Web Api. <br> _This will typically be a client-facing application that forwards client intentions to this API._ | Send requests to be fulfilled.|
| **Server** | Library code: Spark. | Converts incoming HTTP requests into formats easily readable by Java code. |
| **Handler** | App code. Functions connected to endpoints. | Parses out information specific to a particular endpoint and wrap it in a format easily used by the `Service` layer. |
| **Service** | App code. Functions inside classes designated as "services" | Performs validation, logic, updates, and transformation particular to the application. |
| **DataAccess** | App code. Classes implementing interfaces. | Transforms data from an app-friendly format into the format required by a specific database. |
| **Database (db)** | External software: MySQL | Stores and queries data and acts as the source of truth. Supports multiple users changing data simultaneously. |

## Diagram Sample

Click on the image to view the [full diagram][sequence-diagram-link]. A sample of the diagram is provided below for easy reference.

[![PetShop Sequence Diagram — Create Pet](./img/sequence-diagram-sample-create-pet.png)][sequence-diagram-link]

<!-- The Actual Sequence Diagram link is provided below in only a single place.
  This anticipates that updates to the diagram will be likely.
  Multiple places in this document access it with
  [Reference Style Links](https://www.markdownguide.org/basic-syntax/#reference-style-links). -->

[sequence-diagram-link]: https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEXijo8FAocoYACijMAQDuABZIYGKIqKQAtAB85JQ0UABcMADaUQDyZAAqALowAPTYsQA6aADeAERIHB3FTgA0MB1owAC2KL1DzR2DHWCWTZMd0wC+mMIFMDms7FyUxZ3dkwNDI+NL07Pzi8XLHWtsnNyw2xuixcAcHDFgABQ-AEpTDpwsAANYwJCqIwQRy7GCJfQwABm3H063yoi2r3y+xgAFEoN4ijA0BAYfZofEUokUWiNBtKNjculzMUACxOJytTrjdTAewTW6E4nFMkUqk0ukRVQzIaqeRgHyyjlctboDimN4qLa5WTyJQqdQfL4-X5NMBA-WKZRqaHbYzFBRfGAWmA-a0wSDu2JVXTcTDWw125l5EQqYo-DHhwyZbaPPYkqMJ566nZPPEAdRQOjIEBAYNiLlG4JQADkIFCUL8zihBqpfJwgSmmXHcoySYcen1ZrWLnK5gshVN7tHNm34Mg2TAAExcnldbvOXtjYd3K5D-vqzimLy+fwBbA+KDYbgwAAyUOYP2hCWSqUwrKy7dxJJKAHF8bUGhax63ci2JIdB0mCAWm2ooMUyAKjevxAhBaZBraxowNBYCwVa2jBuoaaOjA76xDAwAIAgrqxNCyLeKMZFYEhRr2jiMbFHAEAkSg4BIBAaAADw-NkY5YhOgHMax3AcVxvGxPxYETh2BwdBakwlG0QxHD2pyrv2G43COMArDUawdqGT5gMUc7cu0QwWiqpRdscK7nLclxDNca6rHU26ap43h+IE4QcDIKDcKkPpxDoCCgGCj5Ts+YYFMUJTSPi55fvidSNLE9SFBaACSmpGfGuzPLcIEyS+TEwIK6GxL83TwZiOrbHRdrFFVZp1YGWHIfauR4QR16xJ61JgLSOXSCiVGhZ1BrdaGEGRrEAmNYVGZJotmDEQNzDYOEVCccqCCWDAKAAB5XigmplXF7xGEFsQoO1HD1TGiFdfRxQcHdqSPZhM30bhOjFNIX2GGNE0QNRUYIUJRV4lQlaXbDLwAUjxTZrm+aFmAxalhWVY1qu9aNk9oFI3Nr5OSBBXbCZZlOOyrQjqYQUGKFRjdKSEDbbt+2qIdx1nQqpOreT1B4ncf4vDTMWmTAnIM2gEsaszO67j5B5eMAR3A8FhgKCRoW3kkKRpDL4EU6USUpVUaU-otBUo6tJXC4m5sVZ9uv6wgGFLbGepvS1t2eyRPvNThDqA4FuswF7hvg5D9sNbGK2JsU8PdC7qayRbEvUyyMt0wrSuq0AA
