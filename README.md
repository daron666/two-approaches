# ZLayer Approach

### Motivation

There are couple things why I decided to create this repo. The main thing is "there is no one approach to work with ZLAyers" and it's bad.
When your code grows bigger it's time to think about how to handle its growth and stay sane. I think this approach is something that will help. Idea is to split initialization logic and service logic.

### Thoughts
1. ALWAYS (I mean always) write services as traits. That will help to write different implementations with different dependencies. Like for live, you need real data and for tests, it will be enough to go with the in-memory map for service.
2. Only factory methods should have put requirements into the R hole in ZIO. That's the idea of DI. For example, to be able to create the API service here, you need to provide Cache and Db as an environment, but not for calling Api's methods itself. This will let us more easily separate what service DOES from HOW service can be created.
3. Try to isolate all service-related stuff in one place.

### Pros
1. As you see from code, you have full control over how services' layers are being created, moreover you can add some more custom logic into such methods. For example, here I add some data while I create the Database service. It's super useful especially for tests to have such ability. Cause you can add some records as I did. Or moreover provide some map for in-memory implementation. So it's super useful IMHO.
2. All the logic and all the requirements are being stored in one file. You don't have to remember bigger signatures or whatever. Fewer things to consider == fewer places for making errors.
3. You can have more than 1 layer for each service. (Prod, Test, IT)
4. Service traits don't know anything about implementation. Less coupled code is easy to extend and maintain.
