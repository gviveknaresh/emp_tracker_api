Q1 — How would you scale this for 500 concurrent managers running reports?

1. Run multiple Spring Boot instances:-
   The app is stateless, so we can containerize it and run multiple Spring Boot instances behind a load balancer.
   We can auto-scale based on CPU/memory usage.

2. Proper Caching and Indexing:-
   methods like "getCycleSummary(Long cycle)" will introduce a lot of overhead cause of multiple queries and running
   Synchronously.
   rather than on the fly calculations it's better to maintain separate table and update the avg through an Async method
   or Outbox pattern which will not slow the insert query down.
   Proper indexing, creating a composite index like rating and cycleId will improve the performance rather well
   but performanceReviewEntity will be write heavy considering its performance Season multiple indexes will slow the
   saving as we are already indexing 2 of the fields
   Caching the method response using Spring’s @Cacheable or an in-memory cache like Caffeine will be better than using
   redis to cache db responses, as we are using multiple queries in this specific case.

3. Increasing pool size, introducing read replicas:-
   If system still struggles under high concurrency, we can introduce read replicas for read-heavy operations like
   reports.
   This offloads read traffic from the primary database and we can also increase database connection pool size so each instance can
   handle
   more simultaneous queries.

Q2 — GET /cycles/{id}/summary is slow at 100k+ reviews??

1. Introducing Composite Indexing:
   The schema has "idx_reviews_cycle" on cycle_id alone. At 100k rows, Postgres will use that index to find all reviews
   for the cycle,
   then filter and sort in memory. As row count grows, this gets worse.We can Fix it with a composite index:
   performance_reviews(cycle_id, rating DESC)
2. Introduce Caching:
   on high concurrency it's better to create a single more complex query and caching the values to be updated in
   intervals.
   though it will be difficult to maintain.
3. Maintaining a Separate Analytical Summary Tables:
   this will help to fetch values like average and counts quicker than doing calculations on the fly.

Q3 — Where would you add caching?

1. Cache the full CycleSummaryResponseDTO :
   The result of getCycleSummary() — average rating, top 10 performers, goal counts — does not change until someone
   submits a new review. Add:
   @Cacheable(value = "cycle-summary", key = "#cycleId")
   public CycleSummaryResponseDTO getCycleSummary(Long cycleId)
   we can update it in a TTL: 5–10 minutes as a safety net.
2. Cache existsById(cycleId) :
   Cycles are created once and rarely touched. Caching this cuts one DB call per every summary request.

3. Don't cache fetchReviewsByEmployeeId :
   This is already paginated and is employee-specific, frequently updated. Caching paginated queries with dynamic
   parameters is messy
   and the DB query with the existing idx_reviews_employee index is already fast.
   saveEmployee, filterEmployees — these are write paths or filter queries with many parameter combinations. Cache hit
   rate would be too low to be useful.
   Setup: Add spring-boot-starter-cache + Redis to pom.xml, configure RedisCacheManager with a default TTL,
   and the annotations above just work. No structural changes to the service layer needed.
