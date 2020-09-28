# 悟空CRM-11.0

### 介绍



悟空CRM在中国的开源管理软件行业有较高的知名度。目前软件已达到千万级的用户量，开源系统下载量达到200多万次，已覆盖设计制造业、服务业、金融业、政府机构等多种行业。先后与阿里巴巴、腾讯、CCTV、OPPO、航天信息等多家知名企业达成战略合作。

公司先后获得河南省高新技术企业、国家3A信用企业、IOS9001软件产品认证等20多项荣誉奖项。拥有50余项软件著作权。 获得20余家国内媒体报道。公司自成立以来，以高科技为起点，以技术为核心、 以完善的售后服务为后盾，秉承稳固与发展、求实与创新的精神，已为国内外上万家企业提供了服务。 在为实现企业价值最大化的过程中， 实现了自身的价值的提升，取得了最大程度的双赢合作，并获得了社会各界的广泛赞誉和认同。


### 项目目录结构


``` lua

72_crm
├── admin         -- 系统管理模块和用户管理模块
├── authorization -- 鉴权模块，目前仅用于登录鉴权，后期可能有更改
├── bi            -- 商业智能模块
├── core          -- 通用的代码和工具类
├── crm           -- 客户管理模块
├── email         -- 邮箱模块
├── gateway       -- 网关模块
├── job           -- 定时任务模块
├── km            -- 定时任务模块
├── oa            -- OA模块
└── work          -- 项目管理模块


```

###  项目中主要使用的技术栈


| 名称                  | 版本                        | 说明 |
|---------------------|---------------------------|----|
| spring-cloud-alibaba| 2.2.1.RELEASE(Hoxton.SR3) |  核心框架  |
| swagger             | 2.9.2                     |  接口文档  |
| mybits-plus         | 3.3.0                     |  ORM框架  |
| sentinel            | 2.2.1.RELEASE             |  断路器以及限流  |
| nacos               | 2.2.1.RELEASE             |  注册中心以及分布式配置管理  |
| seata               | 1.2.0                     |  分布式事务 |
| elasticsearch       | 2.2.5.RELEASE(6.8.6)      |  搜索引擎中间件  |
| jetcache            | 2.6.0                     |  分布式缓存框架  |
| xxl-job             | 2.1.2                     |  分布式定时任务框架  |
| gateway             | 2.2.2.RELEASE             |  微服务网关        |
| feign               | 2.2.2.RELEASE             |  服务调用        |


### 悟空CRM项目架构图


![项目架构图](https://images.gitee.com/uploads/images/2020/0910/094237_e7cb3bca_1096736.jpeg "项目架构图.jpg")


### 安装及使用说明

安装悟空CRM需安装以下前置文件，前置文件可在对应的官方下载。


 **一、	前置环境 <br/>** 

-	Jdk1.8
-	Maven3.5.0+   
-	Mysql5.7.20 （<a href="https://gitee.com/myzw/wk_crm/wikis/mysql%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E" target="_blank">数据库安装注意事项</a>）
-	Redis(版本不限)
-	Elasticsearch 6.8.6 （<a href="https://gitee.com/myzw/wk_crm/wikis/elasticsearch%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E" target="_blank">安装注意事项</a>）
-	Seata（1.2.0）
-	Sentinel（1.7.2）
-	Nacos（1.2.1)

 **二、	安装说明 <br/>** 
     
- 导入初始化sql,目前项目下gateway使用的独立数据库，其他模块暂时用的是同一数据库，后续可能进行拆分
      
        1、初始化gateway模块  导入 db/config_info_route.sql
        4、初始化其余模块     导入 db/wk_crm.sql

    - 在项目根目录执行mvn install
    - 然后在各个项目修改数据库以及redis配置文件,默认使用的是application-dev.yml,打包后启动脚本默认使用的application-test.yml配置
    - 在core\src\main\resources\application-core.yml里面
      修改elasticsearch配置

        spring.elasticsearch.rest.uris = elasticsearch地址 例：127.0.0.1:9200
        spring.elasticsearch.rest.username = elasticsearch用户名 例：elastic
        spring.elasticsearch.rest.password = elasticsearch用户名 例: password 

      修改文件上传地址
        crm.upload.config = 文件上传配置 1 本地 2 阿里云OSS 例 ：1
        crm.upload.oss  oss上传文件所需配置内容 crm.upload.oss.bucketName 需要配置两个bucket，
                        0为登录才可访问文件上传地址，1为完全公开文件上传地址
        crm.upload.oss  本地上传文件所需配置内容 crm.upload.oss.uploadPath 需要配置两个地址
                        0为登录才可访问文件上传地址，1为完全公开文件上传地址
                        本地还需配置公网地址，指向服务器网关
      修改jetcache缓存配置
            详见 https://github.com/alibaba/jetcache/wiki
    - 项目日志文件在core\src\main\resources\logback-spring.xml修改
2. 启动 <br/>
        - 先启动nacos,seata,sentinel, elasticsearch,mysql,redis等基础服务 <br/>
	- 再启动各个服务，最少需要启动的服务（gateway，authorization，admin）<br/>
        - 初始化其他依赖框架配置 <br/>
          1、修改seata配置详见，[详见](http://)
          2、修改xxl-job配置，详见[初始化xxl-job](https://gitee.com/myzw/wk_crm/wikis/seata?sort_id=2827577)

3. 打包部署 <br/>
        ·在项目根目录下执行 mvn clean -Dmaven.test.skip=true  package <br/>
        ·然后把对应项目下target文件夹下 <br/>
        ·${name}-${version}-SNAPSHOT.zip/tar.gz上传到服务器，如 admin-0.0.1-SNAPSHOT.zip <br/>
        ·将压缩文件解压，执行sh 72crm.sh start,windows下直接运行72crm.bat <br/>
        ·项目启动默认使用的application-test.yml，application-core.yml,如需修改，可修改对应脚本文件，如：<br/>
        ·spring.profiles.include=core,prod <br/>

4. 其他说明 <br/>
    - 代码生成器在core\src\test下,core\src\test\com\kakarote\generator\Generator.java
    - 接口文档地址
        http://localhost:8443/swagger-ui.html
        或者访问对应服务下 http://服务地址:端口/swagger-ui.html

    - crm初次启动需要初始化elasticsearch索引库<br/>
      初始化方法为：<br/>
        开启crm服务 <br/>
	然后直接通过浏览器访问 网关地址/crmField/initData?type=? <br/>
        如: http://localhost:8443/crmField/initData?type=1 <br/>
	type取值为   1：线索，2: 客户，3：联系人，4：产品，5: : 商机，6：合同，7：回款，9:：公海，17：回访 <br/>
        第一次启动不需要初始化公海模块 <br/>
	以后因数据迁移等需要重新操作可以重新调用本接口进行初始化 <br/>
	初始化前需要去elasticsearch删除对应索引 <br/>
	项目部署到正式环境建议此方法删除或加密 <br/>
	Elasticsearch只是保存了数据库的一个副本方便查询，所有数据以数据库为准 <br/>

4. 模块依赖关系 <br/>

     **除网关外，其余项目均依赖于admin模块** ，用于获取当前登录人的信息<br/>
     oa模块的任务依赖于work模块，其余一些关联业务功能依赖于crm模块 <br/>
     商业智能依赖crm,oa模块<br/>


三、 功能预览

    截图截图


四、xxxx> 这里输入引用文本