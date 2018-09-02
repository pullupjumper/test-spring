# Spring boot配置  
#### application.properties配置 
```
## 資料庫訪問配置  
spring.datasource.driver-class-name = com.mysql.jdbc.Driver  
spring.datasource.url = jdbc:mysql://localhost:3306/db01?useUnicode=true&characterEncoding=utf-8&useSSL=false  
spring.datasource.username = root  
spring.datasource.password = 123

#指定bean所在包
mybatis.type-aliases-package=com.example.demobatis.entity
#指定映射檔
mybatis.mapperLocations=classpath:mapper/*.xml

#mapper
#mappers 多個介面時逗號隔開
mapper.mappers=com.example.demobatis.util.MyMapper
mapper.not-empty=false
mapper.identity=MYSQL

#pagehelper
pagehelper.helperDialect=mysql
pagehelper.reasonable=true
pagehelper.supportMethodsArguments=true
pagehelper.params=count=countSql
```
#### pom.xml  
```
<dependencies>
<!--通用mapper-->
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper-spring-boot-starter</artifactId>
    <version>1.1.5</version>
</dependency>
<!--pagehelper 分頁外掛程式-->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.2.3</version>
</dependency>
</dependencies>


<build>
    <plugins>
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.3.5</version>
    <dependencies>
        <!--配置這個依賴主要是為了等下在配置mybatis-generator.xml的時候可以不用配置classPathEntry這樣的一個屬性，避免代碼的耦合度太高-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.44</version>
        </dependency>
        <dependency>
            <groupId>tk.mybatis</groupId>
            <artifactId>mapper</artifactId>
            <version>3.4.0</version>
        </dependency>
    </dependencies>
    <executions>
        <execution>
            <id>Generate MyBatis Artifacts</id>
            <phase>package</phase>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <!--允許移動生成的檔 -->
        <verbose>true</verbose>
        <!-- 是否覆蓋 -->
        <overwrite>true</overwrite>
        <!-- 自動生成的配置 -->
        <configurationFile>src/main/resources/mybatis-generator.xml</configurationFile>
    </configuration>
</plugin>

    </plugins>
</build>
```
#### MyMapper  
通用Mapper都可以極大的方便開發人員,對單表封裝了許多通用方法，省掉自己寫增刪改查的sql。  
通用Mapper外掛程式網址：https://github.com/abel533/Mapper  
```
package com.example.demobatis.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
```
這裡實現一個自己的介面,繼承通用的mapper，關鍵點就是這個介面不能被掃描到，不能跟dao這個存放mapper檔放在一起。  
最後在啟動類中通過MapperScan注解指定掃描的mapper路徑：  
```
package com.example.demobatis;

import com.example.demobatis.util.MyMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement  // 啟注解事務管理，等同於xml配置方式的 <tx:annotation-driven />
@MapperScan(basePackages = "com.example.demobatis.mapper", markerInterface = MyMapper.class)
public class DemobatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemobatisApplication.class, args);
    }
}

```
#### mybatis-generator.xml  
這裡配置一下上面提到的mybatis-generator.xml檔,該設定檔用來自動生成表對應的Model,Mapper以及xml,該檔位於src/main/resources下面  
Mybatis Geneator 詳解: http://blog.csdn.net/isea533/article/details/42102297  
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!--載入設定檔，為下面讀取資料庫資訊準備-->
    <properties resource="application.properties"/>

    <context id="Mysql" targetRuntime="MyBatis3Simple" defaultModelType="flat">

        <plugin type="tk.mybatis.mapper.generator.MapperPlugin">
            <property name="mappers" value="com.example.demobatis.util.MyMapper" />
            <!--caseSensitive預設false，當資料庫表名區分大小寫時，可以將該屬性設置為true-->
            <property name="caseSensitive" value="true"/>
        </plugin>

        <!-- 阻止生成自動注釋 -->
        <commentGenerator>
            <property name="javaFileEncoding" value="UTF-8"/>
            <property name="suppressDate" value="true"/>
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>

        <!--資料庫連結位址帳號密碼-->
        <jdbcConnection driverClass="${spring.datasource.driver-class-name}"
                        connectionURL="${spring.datasource.url}"
                        userId="${spring.datasource.username}"
                        password="${spring.datasource.password}">
        </jdbcConnection>

        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <!--生成Model類存放位置-->
        <javaModelGenerator targetPackage="com.example.demobatis.entity" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>

        <!--生成映射檔存放位置-->
        <sqlMapGenerator targetPackage="mapper" targetProject="src/main/resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>

        <!--生成Dao類存放位置-->
        <!-- 用戶端代碼，生成易於使用的針對Model物件和XML設定檔 的代碼
                type="ANNOTATEDMAPPER",生成Java Model 和基於注解的Mapper物件
                type="XMLMAPPER",生成SQLMap XML檔和獨立的Mapper介面
        -->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.example.demobatis.mapper" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>

        <!--生成對應表及類名
        去掉Mybatis Generator生成的一堆 example
        -->
        <table tableName="teacher" domainObjectName="Teacher" enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false">
            <!--<property name="useActualColumnNames" value="true"/>-->
            <generatedKey column="id" sqlStatement="Mysql" identity="true"/>

        </table>
        <table tableName="student" domainObjectName="Student" enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false">
            <!--<property name="useActualColumnNames" value="true"/>-->
            <generatedKey column="id" sqlStatement="Mysql" identity="true"/>

        </table>
    </context>
</generatorConfiguration>

```
#### 定義通用service介面  
```
package com.example.demobatis.util;

import java.util.List;

public interface IService<T> {
    T selectByKey(Object key);

    int save(T entity);

    int delete(Object key);

    int updateAll(T entity);

    int updateNotNull(T entity);

    List<T> selectByExample(Object example);

    //TODO 其他...
}
```
#### 具體實現通用介面類別  
```
package com.example.demobatis.util;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public abstract class BaseService<T> implements IService<T> {

    @Autowired
    protected Mapper<T> mapper;
    public Mapper<T> getMapper() {
        return mapper;
    }

    @Override
    public T selectByKey(Object key) {
        //說明：根據主鍵欄位進行查詢，方法參數必須包含完整的主鍵屬性，查詢準則使用等號
        return mapper.selectByPrimaryKey(key);
    }

    @Override
    public int save(T entity) {
        //說明：保存一個實體，null的屬性也會保存，不會使用資料庫預設值
        return mapper.insert(entity);
    }

    @Override
    public int delete(Object key) {
        //說明：根據主鍵欄位進行刪除，方法參數必須包含完整的主鍵屬性
        return mapper.deleteByPrimaryKey(key);
    }

    @Override
    public int updateAll(T entity) {
        //說明：根據主鍵更新實體全部欄位，null值會被更新
        return mapper.updateByPrimaryKey(entity);
    }

    @Override
    public int updateNotNull(T entity) {
        //根據主鍵更新屬性不為null的值
        return mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<T> selectByExample(Object example) {
        //說明：根據Example條件進行查詢
        //重點：這個查詢支持通過Example類指定查詢列，通過selectProperties方法指定查詢列
        return mapper.selectByExample(example);
    }
}
```
#### 最後執行maven指令自動產生mapper, model  
`mybatis-generator:generate -e`
