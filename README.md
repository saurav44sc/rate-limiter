## Rate Limiter

In this rate limiter, we are using sliding window algorithm. Sliding window algorithm involves maintaining a time stamped log of requests. The Sliding Window is the one of the best rate limiting algorithm because it gives the flexibility to scale rate limiting with good performance. We are storing userId and time in the sliding window.

#### Sample Curl to test the rate limiter
 curl --location --request GET 'http://localhost:8080/api/rate-limiter/test' --header 'User-Id: 1234'

Note: We change the request limit per second in the application.properties file.



