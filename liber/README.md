> Welcome to here
# openGMS@CopyRight
# 介绍
该系统的工作就是通过HTTP请求，调用模型容器提供的Rest风格API
# 前置知识
1. SpringBoot JavaWeb框架
2. MongoDB 数据库
3. Swagger API管理
4. Maven 依赖管理
5. 建议使用Intellij IDEA作为IDE进行开发
## 使用说明
### Build
```
mvn install -Dmaven.test.skip=true // 跳过测试(测试类写起来好辛苦，请务必保证接口无误)，生成jar包 

```
### Run
```
java -jar demo.jar//指定
```
## 代码调试
```
java -jar demo.jar --spring.profiles.active=dev
```
Copyright (c) 2017-present, OpenGMS


[联系邮箱](mailto:sun_liber@126.com) 
