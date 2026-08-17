# JSON and Serialization

🖥️ [Slides](https://docs.google.com/presentation/d/19KUDyTUNK_CUFjRQCSkOLtKEbgiLOiQS/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

🖥️ [Lecture Videos](#videos)

📖 **Required Reading**: [Learn Gson](https://www.tutorialspoint.com/gson/index.htm)

📖 **Optional Reading**: [Douglas Crockford: The JSON Saga](https://www.youtube.com/watch?v=-C-JoyNuQJs)

## JSON

### 🔑 Key points

- Understand how to read and interpret JSON documents.
- Generate a JSON string from the instance variables of a Java object.
- Parse a JSON object and represent the data using a Java object.
- Compare different methods for generating and parsing JSON, including their respective pros and cons.
- Use the Gson library to serialize and deserialize Java objects.

---

JavaScript Object Notation (JSON) was conceived by Douglas Crockford in 2001 while working at Yahoo!. JSON, pronounced like the name "Jason," received official standardization in 2013 and 2017 (ECMA-404, [RFC 8259](https://datatracker.ietf.org/doc/html/rfc8259)).

JSON provides a simple yet effective way to share and store data in a textual representation. Here is an example of a simple JSON document:

```json
{
  "class": {
    "title": "web programming",
    "description": "Amazing"
  },
  "enrollment": ["Marco", "Jana", "فَاطِمَة"],
  "start": "2025-02-01",
  "end": null
}
```

 By design, JSON is easily convertible to and from in memory objects, making it a highly convenient data format for serializing data. Because of its simplicity, standardization, and compatibility, JSON has become one of the most popular data formats in the world.

![jsonTransfer.jpg](jsonTransfer.jpg)


### Format

A JSON document contains one of the following data types:

| Type    | Example                 |
| ------- | ----------------------- |
| string  | "crockford"             |
| number  | 42                      |
| boolean | true                    |
| array   | [null, 42, "crockford"] |
| object  | {"a":1, "b":"crockford"}|
| null    | null                    |

Most commonly, a JSON document contains an object. Objects consist of zero or more key-value pairs. The key is always a string, and the value must be one of the valid JSON data types. Key-value pairs are delimited with commas. Curly braces `{}` delimit an object, square brackets `[]` delimit arrays, and strings are always enclosed in double quotes.


```json
{
  "string": "JSON strings must use double quotes.",
  "number_integer": 42,
  "number_float": 3.14159,
  "number_scientific_notation": 1.05e+3,
  "boolean_true": true,
  "boolean_false": false,
  "null_value": null,
  "unicode": "이모티콘: 😀😆😎🤩🥳",
  "object": {
    "key": "value",
    "description": "Unordered collection of key-value pairs.",
    "nested_object": {
      "status": "Object values can be anything."
    }
  },
  "array": [
    "Ordered list of values.",
    100,
    true,
    { "note": "Arrays can contain anything." },
    [ "Arrays can also be nested." ]
  ]
}
```

JSON is always encoded with [UTF-8](https://en.wikipedia.org/wiki/UTF-8), allowing for the representation of global data and special characters.

## Gson

The `Gson` library was created by Google to support JSON processing in Java. If your project references the Gson library, you can easily convert a Java object to a JSON string (serialization) or a JSON string back into a Java object (deserialization). The following code demonstrates this:

```java
public class GsonExample {
    public static void main(String[] args) {
        var obj = Map.of(
                "name", "perry",
                "year", 2264,
                "pets", List.of("cat", "dog", "fish")
        );

        var serializer = new Gson();

        // Serialize Java object to JSON string
        var json = serializer.toJson(obj);
        System.out.println("JSON: " + json);

        // Deserialize JSON string to Java object
        var objFromJson = serializer.fromJson(json, Map.class);
        System.out.println("Object: " + objFromJson);
    }
}
```

Running this code will produce the following output:

```sh
> java JsonExample
JSON: {"year":2264,"pets":["cat","dog","fish"],"name":"perry"}
Object: {year=2264.0, pets=[cat, dog, fish], name=perry}
```

> [!NOTE]
>
> You do not need to manually install the Gson library for your Chess project. The Phase 0 starter code already includes this dependency.

### Creating Gson TypeAdapters

When you call the `fromJson` method to deserialize JSON into an object, you provide the class type that Gson uses to "rehydrate" the JSON content.

```java
    var serializer = new Gson();
    var objFromJson = serializer.fromJson(json, Map.class);
```

However, if the class you are attempting to create contains fields defined as interfaces or abstract classes, you must help Gson determine which concrete class should be instantiated.

For example, consider serializing a `ChessBoard` class. The board likely contains a 2D array of `ChessPiece` objects. If you have created subclasses of `ChessPiece` (such as `Rook` or `Knight`), standard serialization will capture the fields, but the specific class type may be lost during deserialization. While the piece type might be stored in a `type` field, the JSON itself doesn't inherently know it should be a `Rook` object versus a `Knight` object.

```json
{
   "squares":[
       [{"color":"WHITE","type":"ROOK"},{"color":"WHITE","type":"KNIGHT"}, ... ],
       [null,null,null,null,null,null,null,null],
       ...
       [{"color":"BLACK","type":"ROOK"},{"color":"BLACK","type":"KNIGHT"}, ...]
   ]
}
```

When you deserialize this JSON back into Java, Gson won't know to map the `ChessPiece` data to a specific `Rook` or `Knight` subclass. Instead, it might try to instantiate the base `ChessPiece` (which fails if it is abstract) or fail to populate subclass-specific behavior.

You can solve this by defining a Gson `TypeAdapter` (or `JsonDeserializer`) that specifies exactly how the JSON text should be handled.

To use a custom adapter, use a `GsonBuilder`, register the adapter, and then call `create()`. In the example below, we use a lambda to implement the `JsonDeserializer` interface. The function reads the `type` attribute from the JSON element and uses it to determine which concrete class to instantiate.

```java
public static Gson createSerializer() {
    GsonBuilder gsonBuilder = new GsonBuilder();

    gsonBuilder.registerTypeAdapter(ChessPiece.class,
            (JsonDeserializer<ChessPiece>) (el, type, ctx) -> {
                ChessPiece chessPiece = null;
                if (el.isJsonObject()) {
                    String pieceType = el.getAsJsonObject().get("type").getAsString();
                    switch (ChessPiece.PieceType.valueOf(pieceType)) {
                        case PAWN -> chessPiece = ctx.deserialize(el, Pawn.class);
                        case ROOK -> chessPiece = ctx.deserialize(el, Rook.class);
                        case KNIGHT -> chessPiece = ctx.deserialize(el, Knight.class);
                        case BISHOP -> chessPiece = ctx.deserialize(el, Bishop.class);
                        case QUEEN -> chessPiece = ctx.deserialize(el, Queen.class);
                        case KING -> chessPiece = ctx.deserialize(el, King.class);
                    }
                }
                return chessPiece;
            });

    return gsonBuilder.create();
}
```

**Remember**: You only need a `GsonBuilder` to override default behavior when your object model uses polymorphism (interfaces, abstract classes, or inheritance) in fields that need to be serialized and deserialized.

## ☑ Exercise


````masteryls
{"id":"abf22efc-4823-4ae3-99c5-0847c7a6fd4d","title":"JSON Syntax Validation","type":"multiple-choice"}

Is the following JSON object valid? If not, identify the error.

```json
{
  "id": 2048,
  "status": "success",
  "data": ["item1", "item2"],
}
```

- [ ] No, because the integer value `2048` must be wrapped in double quotes to be a valid JSON number.
- [ ] No, because JSON keys must be defined using single quotes (`'`) instead of double quotes (`"`).
- [x] No, because JSON does not allow trailing commas after the last element in an object or array.
- [ ] Yes, this is a valid JSON object and can be successfully parsed by any standard JSON library.
````


```masteryls
{"id":"1bfb9794-0fab-4bdc-8028-3029fe73365d", "title":"JSON Chess", "type":"essay" }
Create a valid JSON document that represents a chess piece. 
```


## Videos

- 🎥 [JSON Introduction (8:44)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=68705db1-09e9-401a-899a-b170015429ca) - [[transcript]](https://github.com/user-attachments/files/17751128/CS_240_JSON_Introduction_Transcript.pdf)
- 🎥 [Parsing JSON (3:02)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=84e10091-df45-42e8-a244-b1700156f7c0) - [[transcript]](https://github.com/user-attachments/files/17751145/CS_240_Parsing_JSON_Transcript.pdf)
- 🎥 [JSON Stream Parser (14:09)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ce33cc30-4265-4b3c-8cf1-b1700158857a) - [[transcript]](https://github.com/user-attachments/files/17751156/CS_240_JSON_Stream_Parser_Transcript.pdf)
- 🎥 [JSON DOM Parser (9:24)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=799da5b0-57ad-4f43-adac-b170015d3c9e) - [[transcript]](https://github.com/user-attachments/files/17751163/CS_240_JSON_DOM_Parser_Transcript.pdf)
- 🎥 [JSON Object Serialization (12:50)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=11bfdcea-647b-49a2-9c4f-b170016048d5) - [[transcript]](https://github.com/user-attachments/files/17751171/CS_240_JSON_Object_Serialization_Transcript.pdf) _Note: This video mentions adding dependencies in the Maven style. While the autograder uses Maven, loading the project in IntelliJ as a Maven project can sometimes cause issues; please follow the course-specific instructions for adding dependencies._
- 🎥 [Creating GSON Project Dependency (6:06)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7d951c28-2f7e-4e0f-ab83-b1700164a7a7) - [[transcript]](https://github.com/user-attachments/files/17751202/CS_240_Creating_Gson_Project._Dependency_Transcript.pdf)
- 🎥 [GSON Type Adapters (6:01)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=5d389661-eda2-4d90-8168-b17001668914) - [[transcript]](https://github.com/user-attachments/files/17751233/CS_240_Gson_Type_Adapters_Transcript.pdf)
- 🎥 [Complex Type Adapters (11:59)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=5e845943-a45f-430b-8510-b1700168544d) - [[transcript]](https://github.com/user-attachments/files/17751262/CS_240_Complex_Type_Adapter_Transcript.pdf)