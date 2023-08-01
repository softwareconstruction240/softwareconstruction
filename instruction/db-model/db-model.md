# Relational Databases - The Relational Model

🖥️ [Slides](https://docs.google.com/presentation/d/19nC7v6SDqoEeK75Mb-f6L3QhnbuP6Xfo/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

Relational databases are the most common way to persistently store and retrieve data. You can read and write data to a relational database from your program using standard library classes. Before we dive into how you actually use a database, we first want to discuss how relational databases work.

At a basic level, relational data is stored in a `database`. A database contains `tables`. A table has a number of `columns` that define the fields of the table. This can be things like `name`, `phone number`, or `id`. When you insert data into a database table it must match the column definition. Each data item is inserted as a `row`.

The following gives a simple visualization of a database name `pet-store` that contains a table for `pet`, `owner`, and `purchase`.

**pet**
|id|name|type|
|-|-|-|
|93|Fido|dog|
|14|Puddles|cat|
|77|Chip|bird|

**owner**
|id|name|phonenumber|
|-|-|-|
|81|Juan|6196663333|
|82|Bud|8018889999|

**purchase**
|id|ownerid|petid|
|-|-|-|
|51|81|93|
|51|82|77|

With you data stored in relational tables you can use the different ID fields of the table to cross-reference, or join, the data together to power your application. We will learn how to read, write, and query your data when we discuss Structure Query Language (SQL).

Things to understand:

- How data is represented in the relational model
- How primary and foreign keys work
- How to represent one-to-one, one-to-many, and many-to-many relationships using primary and foreign keys
- What makes a good primary key
- How to model inheritance relationships in the relational model
- How to represent a data model in an ERD

## Videos

- 🎥 [Relational Databases Overview](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=10667c35-dea3-4f1e-8c91-ad66013d553b&start=0)
- 🎥 [Understanding the Relational Model](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=3ec3f6de-a112-4e0a-a0af-ad66013f8bc7&start=0)
- 🎥 [Modeling a Database Schema](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ee130025-e1ab-4f6b-a72c-ad660143e8aa&start=0)
- 🎥 [Modeling Inheritance Relationships](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6bb9d1f1-803c-4d8f-a5ea-ad660146883e&start=0)
