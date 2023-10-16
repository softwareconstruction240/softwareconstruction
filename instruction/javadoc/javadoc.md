# Javadoc

## What is Javadoc?


Javadoc is a tool that comes with JDK and it is used for generating Java code documentation in HTML format from Java source code, which requires documentation in a predefined format.

To use Javadoc, put "Javadoc comments" on all classes, fields, constructors, and methods that require documentation. A "Javadoc comment" begins with `/**` and ends with `*/`. A minimal Javadoc comment includes a description of the class, field, constructor, or method being documented. A Javadoc comment may also contain "Javadoc tags" that can be used to provide more specific information. Here is a list of the most commonly used tags:

| Common Tags | Description | Format | Example |
| --- | --- | --- | --- |
| `@param` | Used to document a constructor or method parameter's name and purpose | `@param paramName paramDescription` | `@param id the id of the customer who created the order` |
| `@throws` | Used to document an exception type that might be thrown by a constructor or method | `@throws exceptionClassName description` | `@throws SQLException the order could not be added to the database` |
| `@exception` | Same as `@throws` | `@exception exceptionClassName description` | `@exception SQLException the order could not be added to the database` |
| `@return` | Used to document a method's return value | `@return descriptionOfReturnValue` | `@return the customer with the specified id` |

## Javadoc Examples

Here are some example classes that include Javadoc comments. Notice that for CS 240 you do not need to comment getter and setter methods, nor do you need to comment toString, equals, and hashCode methods.

```java
/**
 * A book.
 */
public class Book {

    /**
     * The unique id.
     */
    private int id;

    /**
     * The title.
     */
    private String title;

    /**
     * The person who wrote the book.
     */
    private String author;

    /**
     * The genre (or type).
     */
    private String genre;

    /**
     * An identifier for the book's category
     */
    private int categoryId;

    /**
     * Creates a book without a book id.
     *
     * @param title the book title.
     * @param author the person who wrote the book.
     * @param genre the genre of the book.
     * @param categoryId an identifier for the book's category
     */
    public Book(String title, String author, String genre, int categoryId) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.categoryId = categoryId;
    }

    /**
     * Creates a book with a book id.
     *
     * @param id the unique id of the book.
     * @param title the book title.
     * @param author the person who wrote the book.
     * @param genre the genre of the book.
     * @param categoryId an identifier for the book's category
     */
    public Book(int id, String title, String author, String genre, int categoryId) {
        this(title, author, genre, categoryId);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return getId() == book.getId() &&
                getCategoryId() == book.getCategoryId() &&
                Objects.equals(getTitle(), book.getTitle()) &&
                Objects.equals(getAuthor(), book.getAuthor()) &&
                Objects.equals(getGenre(), book.getGenre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getAuthor(), getGenre(), getCategoryId());
    }
}
```

```java
/**
 * An example that shows the main JDBC operations, including creation of a connection, use of transactions, and the
 * following database operations: insert, update, delete and select.
 */
public class DatabaseAccessExample {

    /**
     * Runs the example.
     *
     * @param args not used.
     */
    public static void main(String [] args) {
        try {
            DatabaseAccessExample example = new DatabaseAccessExample();
            List<Book> books = example.doTransaction();
            System.out.println("Final Book List:");
            example.printBooks("\t", books);
        } catch (SQLException e) {
            System.err.println("Unable to list books because of exception " + e.getMessage());
        }
    }

    /**
     * Performs a database transaction which includes clearing the book table, reseting the auto-increment primary key
     * sequence of the book table, inserting some books, updating the author of a book, and retrieving the resulting
     * list of books.
     *
     * @return the resulting list of books.
     * @throws SQLException if an SQL error occurs.
     */
    public List<Book> doTransaction() throws SQLException {

        List<Book> books;
        Connection connection = null;

        try {
            // Open a database connection
            String dbName = "db" + File.separator + "bookclub.sqlite";
            String connectionURL = "jdbc:sqlite:" + dbName;
            connection = DriverManager.getConnection(connectionURL);

            // Start a transaction
            connection.setAutoCommit(false);

            // Remove all books
            removeBooks(connection);
            System.out.println();

            // Insert books
            insertBooks(connection, getBooksToInsert());
            System.out.println();

            // Retrieve books
            books = getBooks(connection);
            System.out.println("Retrieved the following books from the database:");
            printBooks("\t", books);
            System.out.println();

            // Change the author of the first book
            Book book = books.get(0);
            book.setAuthor("Jerod Wilkerson");
            updateBook(connection, books.get(0));
            System.out.println();

            // Commit the transaction
            connection.commit();
        } catch(SQLException ex) {
            if(connection != null) {
                connection.rollback();
            }
            throw ex;
        } finally {
            if(connection != null) {
                connection.close();
            }
        }

        return books;
    }

    /**
     * Generates a list of books to be inserted in the database.
     *
     * @return the books.
     */
    private List<Book> getBooksToInsert() {
        List<Book> books = new ArrayList<>();

        books.add(new Book("Decision Points", "George W. Bush", "NonFiction", 7));
        books.add(new Book("The Work and the Glory", "Gerald Lund", "HistoricalFiction", 3));
        books.add(new Book("Dracula", "Bram Stoker", "Fiction", 8));
        books.add(new Book("The Holy Bible", "The Lord", "NonFiction", 5));

        return books;
    }

    /**
     * Prints a list of books by calling {@link Book#toString()} on each book and pre-pending the resulting string with
     * the specified prefix. The prefix is useful for indenting the printout of books.
     *
     * @param prefix the prefix inserted before each book in the printout.
     * @param books the books to be printed.
     */
    private void printBooks(String prefix, List<Book> books) {
        for(Book book : books) {
            System.out.println(prefix + book);
        }

    }

    /**
     * Removes all books from the database and resets the auto-increment primary key sequence.
     *
     * @param connection the connection to be used for this database operation.
     * @throws SQLException if an SQL error occurs.
     */
    private void removeBooks(Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "delete from book";
            stmt = connection.prepareStatement(sql);

            int count = stmt.executeUpdate();

            // Reset the auto-increment counter so new books start over with an id of 1
            sql = "delete from sqlite_sequence where name = 'book'";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();

            System.out.printf("Deleted %d books\n", count);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * Inserts the specified books into the database.
     *
     * @param connection the connection to be used for this database operation.
     * @param books the books to be inserted.
     * @throws SQLException if an SQL error occurs.
     */
    private void insertBooks(Connection connection, List<Book> books) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "insert into book (title, author, genre, category_id) values (?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql);

            for(Book book : books) {
                stmt.setString(1, book.getTitle());
                stmt.setString(2, book.getAuthor());
                stmt.setString(3, book.getGenre());
                stmt.setInt(4, book.getCategoryId());

                stmt.executeUpdate();
                System.out.println("Inserted book " + book);
            }

        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * Retrieves all books from the database.
     *
     * @param connection the connection to be used for the database operation.
     * @return the books.
     * @throws SQLException if an SQL error occurs.
     */
    private List<Book> getBooks(Connection connection) throws SQLException {

        List<Book> books = new ArrayList<>();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "select id, title, author, genre, category_id from book";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String author = rs.getString(3);
                String genre = rs.getString(4);
                int categoryId = rs.getInt(5);

                books.add(new Book(id, title, author, genre, categoryId));
            }
        } finally {
            if(rs != null) {
                rs.close();
            }

            if(stmt != null) {
                stmt.close();
            }
        }

        return books;
    }

    /**
     * Updates the book with the ID of the specified book, setting all values to the current values of the specified book.
     *
     * @param connection the connection to be used for the database operation.
     * @param book the book to be updated.
     * @throws SQLException if an SQL error occurs.
     */
    private void updateBook(Connection connection, Book book) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "update book " +
                    "set title = ?, author = ?, genre = ?, category_id = ? " +
                    "where id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getCategoryId());
            stmt.setInt(5, book.getId());

            if(stmt.executeUpdate() == 1) {
                System.out.println("Updated book " + book.getId());
            } else {
                System.out.println("Failed to update book " + book.getId());
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
```

## Running Javadoc in IntelliJ

After creating and commenting your classes, follow these instructions to generate the documentation for your code.

1. Select packages you want Javadocs for
1. Select Tools -> Generate JavaDoc...
1. Create an empty directory that will contain the files created by Javadoc, and Select it as the output directory
1. Select "private" on the private/package/protected/public scale
1. Deselect "Include test sources"
1. Click OK. Javadocs will be in the specified output directory.
1. Compress Javadocs into a zip archive
