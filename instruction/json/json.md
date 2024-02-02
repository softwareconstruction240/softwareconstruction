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

By default Gson will attempt to examine the class you are importing and exporting and properly represent all the fields of the class. However, if you are exporting or importing an abstract class then it will not export all of the fields of the subclass. You may also want to have control over what Gson imports and exports. In order to support these use cases, Gson provides the ability to supply a `TypeAdapter` that implements exactly how the JSON text should be serialized and deserialized.

To define a `TypeAdapter` you write a class that implements a `write` and `read` method. The following is a simple type adapter that prefixes a string on output and removes the prefix on input.

```java
public static TypeAdapter<String> createPrefixAdapter(String prefix) {
    return new TypeAdapter<>() {
        @Override
        public void write(JsonWriter w, String text) throws IOException {
            w.value(prefix + text);
        }

        @Override
        public String read(JsonReader r) throws IOException {
            var text = r.nextString().substring(prefix.length());
            return text;
        }
    };
}
```

You can then use the type adapter when you create your Gson serializer by creating a `GsonBuilder` and registering the type adapter. In the following example we register the adapter to work with any `String` objects.

```java
var builder = new GsonBuilder();
builder.registerTypeAdapter(String.class, createPrefixAdapter("x-"));
var serializer = builder.create();
```

The new serializer will then call the adapter whenever it attempts to serialize objects of the type the adapter is registered for. Here is the full example.

```java
public class GsonAdapterExample {

    public static void main(String[] args) {
        var obj = new String[]{"cat", "dog", "cow"};

        var builder = new GsonBuilder();
        builder.registerTypeAdapter(String.class, createPrefixAdapter("x-"));
        var serializer = builder.create();

        var json = serializer.toJson(obj);
        System.out.println("JSON:   " + json);

        var objFromJson = serializer.fromJson(json, obj.getClass());
        System.out.println("Object: " + Arrays.toString(objFromJson));
    }


    public static TypeAdapter<String> createPrefixAdapter(String prefix) {
        return new TypeAdapter<>() {
            @Override
            public void write(JsonWriter w, String text) throws IOException {
                w.value(prefix + text);
            }

            @Override
            public String read(JsonReader r) throws IOException {
                var text = r.nextString().substring(prefix.length());
                return text;
            }
        };
    }
}
```

The above code will output the following.

```sh
JSON:   ["x-cat","x-dog","x-cow"]
Object: [cat, dog, cow]
```

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

📁 [deserializer](example-code/deserializer)

📁 [runtimeTypeAdapter](example-code/runtimeTypeAdapter)
