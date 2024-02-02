# JSON and Serialization

🖥️ [Slides](https://docs.google.com/presentation/d/19KUDyTUNK_CUFjRQCSkOLtKEbgiLOiQS/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Required Reading**: [Learn Gson](https://www.tutorialspoint.com/gson/index.htm)

📖 **Optional Reading**: [Douglas Crockford: The JSON Saga](https://www.youtube.com/watch?v=-C-JoyNuQJs)

## JSON

JavaScript Object Notation (JSON) was conceived by Douglas Crockford in 2001 while working at Yahoo! JSON, pronounced like the name Jason, received official standardization in 2013 and 2017 (ECMA-404, [RFC 8259](https://datatracker.ietf.org/doc/html/rfc8259)).

JSON provides a simple, and yet effective way, to share and store data. By design JSON is easily convertible to, and from, JavaScript objects. This make it a very convenient data format when working with web technologies. Because of its simplicity, standardization, and compatibility with JavaScript, JSON has become one of the world's most popular data formats.

### Format

A JSON document contains one of the following data types:

| Type    | Example                 |
| ------- | ----------------------- |
| string  | "crockford"             |
| number  | 42                      |
| boolean | true                    |
| array   | [null,42,"crockford"]   |
| object  | {"a":1,"b":"crockford"} |
| null    | null                    |

Most commonly, a JSON document contains an object. Objects contain zero or more key value pairs. The key is always a string, and the value must be one of the valid JSON data types. Key value pairs are delimited with commas. Curly braces delimit an object, square brackets and commas delimit arrays, and strings are always delimited with double quotes.

Here is an example of a JSON document.

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

JSON is always encoded with [UTF-8](https://en.wikipedia.org/wiki/UTF-8). This allows for the representation of global data.

## Gson

The `Gson` library was created by Google in order to support JSON in Java code. Once you have installed the Gson library you can convert a Java String to a Java object, or a Java object to a string. The following code demonstrates how to do this.

```java
public class GsonExample {
    public static void main(String[] args) {
        var obj = Map.of(
                "name", "perry",
                "year", 2264,
                "pets", List.of("cat", "dog", "fish")
        );

        var serializer = new Gson();

        var json = serializer.toJson(obj);
        System.out.println("JSON: " + json);

        var objFromJson = serializer.fromJson(json, Map.class);
        System.out.println("Object: " + objFromJson);
    }
}
```

Running this code will output the following.

```sh
> java JsonExample
JSON: {"year":2264,"pets":["cat","dog","fish"],"name":"perry"}
Object: {year=2264.0, pets=[cat, dog, fish], name=perry}
```

### Creating Gson TypeAdapters

When you call the `fromJson` method to deserializing JSON into an object, you provide the class that it will use to rehydrate the JSON content.

```java
    var serializer = new Gson();
    var objFromJson = serializer.fromJson(json, Map.class);
```

However, if the class that you are attempting to create contains fields that are interfaces or derived classes, then you must help Gson know which class should be used when it creates the backing class for the interface or derived class.

For example, consider serializing the `ChessBoard` class. The chess board probably contains a field that contains a double array of `ChessPieces`. If you have created subclasses of `ChessPiece`, with things like `Rook` or `Knight`, and when you serialize out your board, the JSON will lose the representation of the derived classes. The type of piece is still represented in the `type` field, but there is not `Rook` or `Knight` class representation and the JSON will look something like the following.

```json
{
   "squares":[
       [{"color":"WHITE","type":"ROOK"},{"color":"WHITE","type":"KNIGHT"}, ... ]
       [null,null,null,null,null,null,null,null],
       [null,null,null,null,null,null,null,null],
       ...
       [{"color":"BLACK","type":"ROOK"},{"color":"BLACK","type":"KNIGHT"}, ...]
   ]
}
```

You now have a problem when you deserialize the JSON, Gson won't know that it should turn the `ChessPiece` class described by the `ChessBoard` into specific `Rook`, `Knight` or any other classes that you had before you serialized the board. So Gson will just create a bunch of `ChessPiece` objects in your board array.

You can solve this by defining a Gson `TypeAdapter` that implements exactly how the JSON text should be deserialized.

To use a `TypeAdapter` you create a `GsonBuilder`, register the type adapter with the builder, and create the Gson serializer from the builder with the `create` method. In the example below, we create a lambda function that implements the `JsonDeserializer` interface of the `TypeAdapter`. The function reads the JSON `type` attribute from the data contained in the JSON element represented by the `el` parameter. This allows the deserializer to switch on what class actually gets created form the JSON element.

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

Remember that you only need to use a GsonBuilder to override the default Gson serialization functionality if you want different classes to be used when you deserialize your JSON back into Java objects. This is usually because you are using interfaces, abstract classes, or derived classes in fields of your serialized object.

## Obtaining the Gson Dependency

There are three main ways to make the Gson library available to your project:

1. Add the dependency from Intellij's File / Project Structure dialog

   Search for gson and select the latest version

1. Create a Maven project and add the Maven dependency to your pom.xml file

   <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>2.10.1</version>
   </dependency>

1. Create a Gradle project and add the Gradle dependency to your build.gradle file

   implementation group: 'com.google.code.gson', name: 'gson', version: '2.10.1'

## Things to Understand

- How to read / understand JSON documents
- How to generate an JSON string from the instance variables of a Java object
- How to parse an JSON object and represent the data with a Java object
- Different ways to generate and parse JSON objects and the pros and cons of each
- How to use the Gson library to serialize and deserialize a Java object

## Videos

⚠ Note: The following videos reference XML. This data format is no longer used in this class and so you can skip past those parts.

- 🎥 [Data Formats]()
- 🎥 [Parsing and Serializing]()

## Demonstration code

📁 [domain](example-code/domain)

📁 [generator](example-code/generator)

📁 [parser](example-code/parser)

📁 [typeAdapter](example-code/typeAdapter)

📁 [runtimeTypeAdapter](example-code/runtimeTypeAdapter)
