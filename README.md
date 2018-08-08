# FastLock

基于redis的高性能分布式锁服务

## 构建
服务采用Gradle构建

## 使用方式
1. 引入client

如果使用Maven构建，则加入如下依赖
```
      <dependency>
          <groupId>com.destiny</groupId>
          <artifactId>fast-lock-api</artifactId>
          <version>0.0.1-SNAPSHOT</version>
      </dependency>

      <dependency>
          <groupId>com.destiny</groupId>
          <artifactId>fast-lock-client</artifactId>
          <version>0.0.1-SNAPSHOT</version>
      </dependency>
```
如果使用Gradle构建，则加入如下依赖
```
compile('com.destiny:fast-lock-api:0.0.1-SNAPSHOT')
compile('com.destiny:fast-lock-client:0.0.1-SNAPSHOT')
```

2. 使用demo
```
@RestController
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private DistributedLock distributedLock;

    @RequestMapping(value = "/test01", method = RequestMethod.GET)
    public String test() throws InterruptedException {
        distributedLock.lock("222222", 10000);
        distributedLock.unlock("222222");
        return "111";
    }
}
```
