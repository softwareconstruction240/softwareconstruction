# Phase 3 TA Tips

> [!NOTE]
> **It is not required reading.**
> This resource has been helpfully gathered to help you as a student by invested TAs.

## Spark Documentation

The documentation for the Spark library is no longer available from the official website (which no longer exists). However, the information still exists on the Internet Archive: [Web Archive > Spark Java Documentation](https://web.archive.org/web/20240620135725/https://sparkjava.com/documentation)

## 5 Pro tips

5 Pro tips for solving your own coding problems (#4 will surprise you):

1. **Read the error messages.** The computer doesn’t lie, and it even tells you exactly which line caused the problem!
2. **Read the specs.** The document is long, but it’s not redundant— it’s helpful. All the information has been carefully placed in there to help YOU as a student.
3. **Search Slack.** It’s really to ask a new question, but first try to use the search feature to find a pre-existing answer.
4. **Write your own tests.** Yes, it was convenient in the first phases when we had done that for you, but now we’re expecting you to do this yourself. The Phase 3 tests are intentionally less helpful than before. Rely on manual testing, or write your own tests from the beginning.
5. **Appreciate the learning opportunity.** We (the instructors) designed this course to teach you new things. You are supposed to wrestle with new concepts and challenges, but it is possible. Think through the problems; you can do it!

## Why is there no distinction between `Unauthorized` and `Unregistered` errors?

> **Question:**
> Would trying to sign in with an unregistered username generate a 401 error or a 500 error?
> my guess is that it's a 401, since it was a user error, but calling it "Unauthorized" when it's more "Unregistered" doesn't feel quite right

While the user isn't registered, you still want to return `Unauthorized`, otherwise you would be "leaking" information.

For example, lets say you return `Unregistered` for users that don't exist and `Unauthorized` for users that do exist but just have the wrong password. If I'm a bad actor and I want to try and log in to someone else's account, if I get an `Unregistered` exception, that tells me that the account I'm trying to log into doesn't exist, and if I get an `Unauthorized` exception that means I have an account that exists, but I don't have the right password. So I could just spam different passwords at accounts I know exist, rather than waste my time with accounts that don't exist.

Returning `Unauthorized` in both cases doesn't "leak" the information whether an account exists or not. Just think about when you try to log into somewhere, and if you put the wrong password, it usually says "wrong username/password" rather than just saying "wrong password."
