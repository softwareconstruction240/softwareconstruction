# Concurrency and Multi-threaded Programming

🖥️ [Slides](https://docs.google.com/presentation/d/1ibtqBjYEzx45Nh9eLP5xq6jWKfVjVMpv/edit#slide=id.p1)

📖 **Optional Reading**: OPTIONAL: Core Java for the Impatient, Chapter 10: Concurrent Programming

In order to understand the value of concurrent programming it is helpful examine a process that execute discrete tasks. For example, a pizza shop takes orders and makes pizzas. A shop that only has one worker can only take one order at a time and make one pizza at a time.

![Single thread](singleThread.gif)

> _1_ - One worker, one customer

This is fine if you only have one customer at a time, but you run into trouble if multiple customers want pizzas all at the same time. Like during a lunch hour rush. With only one employee, the customers will need to wait for each previous customer to be served. This leads to unhappy customers and eventually to a decrease in profit.

![Single thread queued](singleThreadQueue.gif)

> _2_ - One worker, multiple customers

We can solve this problem by hiring more workers so we can serve multiple customers at the same time, or **concurrently**. With two workers we can make pizzas twice as fast. If we add a third worker then we can make pizzas three times as fast.

![Single thread queued](multiThreaded.gif)

> _3_ - Multiple workers, multiple customers

The pattern of concurrent execution of tasks is a foundational principle in computer science that enables increased throughput.

## Concurrency Complexities

Implementing concurrency doesn't come for free. It is more complex to hire and manage multiple workers, and if the workers cannot execute as a team you may lose all of the benefits that concurrency provides and end up with a shop that is less efficient than a shop with a single worker. These complexities can include operational overhead, resource synchronization, starvation, and deadlock. Let's look at each one individually.

### Overhead

With our pizza example, we can keep adding workers in an attempt to increase **throughput**, but at some point the shop will be too small to allow the workers to efficiently move around. When that happens, adding more workers just increase the **overhead** of each worker having to walk around each other. This will decrease the productivity of each worker and cause pizzas to be created at a slower rate. If we continue to add workers, then eventually none of them will be able to move and no pizzas will be created.

### Resource synchronization

You can also run into trouble when the workers need to **synchronize** their work on resources that cannot be shared concurrently, such as the pizza oven or cash register. Imagine what would happen if two workers tried to take and order at the same time using the same cash register. You might end up with one customer paying for another customer's pizza, or both customers getting their pizzas for free.

### Starvation

There is also the problem of one worker monopolizing a resource. For example, if a worker starts taking an order on the cash register, but then decides to go on break before completing the order. Now, no other worker can take any orders. The result is no more pizzas and everyone is unhappy. When workers cannot operate because a necessary resource is not available it is called resource **starvation**.

### Deadlock

In the pizza shop you need to have the paddle to pull a pizza out of the oven and create a pizza box to put it in. If one worker is holding the box maker while a different worker is holding the oven paddle, neither one can actually complete the pizza making process. When two workers each hold a resource that the other worker needs to get a job done, you end up with **deadlock** in your system. One of the workers must temporarily release the resource so that the work can move forward.

## Concurrent Programming in Java

The primary mechanisms that makes concurrent programming work in Java are Processes and Threads. A process is created when you run the Java Virtual Machine and point it at a class that has a `main` function. Once the main process has started it can span other processes using the [ProcessBuilder](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html) object. Each process runs as a separate application that can communicate with other processes using the main function arguments, standard input, standard output, or inter process communication.

A Thread is a light weight process that runs under the context a parent process. This means that threads can share memory, variables or parameters, in order to communicate with each other. You create a Java thread by extending the [Thread](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html) abstract class and providing a `run` method. You then allocate a new object from your class and call the `start` method. This will create a branch in the execution of your code. One branch will start executing your `run` method and the other branch with start executing the code listed after the `start` call.

## Thread Example 

The following program demonstrates creating two thread that print out the thread's ID multiple times. 

```java
public class ThreadExample {
    public static void main(String[] args) {

        new CountingThread().start();
        new CountingThread().start();

        System.out.println("\nExit Main Thread");
    }


    static class CountingThread extends Thread {
        public void run() {
            var id = this.threadId();
            for (int i = 0; i != 10; i++) {
                System.out.printf("%s:%d ", id, i);
            }
            System.out.printf("%nExit thread %s%n", id);
        }
    }
}
```

What this program outputs will be different every time you run it because it relies on the scheduler of your computer's processor and how content is inserted into the output stream. However, one possible output is demonstrated below. Notice that the output for the two threads are intermingled with each other, and that the main process thread exits before the other two threads. This demonstrates the concurrent nature of the execution.

```txt
Exit Main Thread
22:0 22:1 22:2 22:3 22:4 23:0 23:1 23:2 23:3 22:5 22:6 22:7 22:8 22:9 
Exit thread 22
23:4 23:5 23:6 23:7 23:8 23:9 
Exit thread 23
```

### Runnable

You can also skip extending 'Thread' and use the `Runnable` functional interface to run a thread. This allows you to compactly represent your thread implementation with a lambda function.

```java
public class RunnableExample {
    public static void main(String[] args) {

        new Thread(() -> {
            var id = Thread.currentThread().threadId();
            for (int i = 0; i != 10; i++) {
                System.out.printf("%s:%d ", id, i);
            }
        }).start();

        System.out.println("\nLeaving Main Thread");
    }
}
```

### Callable

Sometimes you need to wait for a thread to calculate a result before you continue executing your main process thread. You can do this by creating an [ExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html) that handles the execution of a pool of threads. You can then submit a lambda function that implements the `Callable` functional interface. Callable is similar to Runnable, except that it returns a result. The result is represented by a `Future` object that will eventually contain the result of the Callable once it exits.

You can wait for the Callable to complete, and thus get the return value, by calling the `get` method on the future object. This is demonstrated by the code below. Calling the `submit` method causes the thread to branch but the main thread will block with the `get` call until the thread completes. 

```java
public class CallableExample {
    public static void main(String[] args) throws Exception {
        try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            Future<String> future = executorService.submit(() -> {
                return "Callable result";
            });
            System.out.println(future.get());
        }
    }
}
```

### Thread Pools

The ExecutorService's pool of threads allows you to efficiently execute threads while minimizing the overhead of creating and switching between threads. In our example above we created a pool using the `Executors` factory method `newSingleThreadExecutor` that shares a single thread and simply time swaps between them. You can also create a thread pool that allocates a fixed number of threads using the `newFixedThreadPool` method, or a thread pool that grows, and reuses threads, when new threads are requested with the `newCachedThreadPool`.

|Pool Type|Description|
|-|-|
|newSingleThreadExecutor|Uses a single thread and switches the callable task. Good for removing thread context switching overhead.|
|newFixedThreadPool|Reuses threads. Good for saving on thread creation overhead.|
|new CachedThreadPool|Reuses threads. Good for saving on thread creation overhead where the maximum number of needed thread is unknown.|
|newScheduledThreadPool|Runs threads periodically. Good for scheduled tasks.|

## Race Conditions

## Mutexes

## Synchronization

## Starvation

## Database Transactions

## Threading Overhead

## Things to Understand

- What is a thread?
- Creating and executing a simple thread in Java
- Using a thread pool (ExecutorService in Java) to run multiple threads
- What is a race condition (or race hazard)?
- How to use database transactions to avoid race conditions
- How to use "synchronized" methods and code blocks in Java to avoid race conditions
- How to avoid race conditions in the Chess server and client programs

## Demonstration code

📁 [Java Thread Example](example-code/src/demo/JavaThreadExample.java)

📁 [Java Thread Pool Example](example-code/src/demo/JavaThreadPoolExample.java)

📁 [File Race Condition Example](example-code/src/demo/FileRaceConditionExample.java)

📁 [Synchronized Stack Example](example-code/src/demo/Stack.java)
