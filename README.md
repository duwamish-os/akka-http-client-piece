[fork-join pool](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ForkJoinPool.html)
---------------

```
A ForkJoinPool differs from other kinds of `ExecutorService` mainly by virtue of employing work-stealing:

all threads in the pool attempt to **find and execute tasks submitted** to the pool and/or
created by other active tasks (eventually blocking waiting for work if none exist).

This enables efficient processing when most tasks spawn other subtasks (as do most ForkJoinTasks),
as well as when many small tasks are submitted to the pool from external clients.

Especiallywhen setting asyncMode to true in constructors, ForkJoinPools may also be appropriate for use with
event-style tasks that are never joined.
```
