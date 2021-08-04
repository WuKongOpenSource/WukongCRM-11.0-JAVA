package com.kakarote.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Generator {
    public static void main(String[] args) {
        System.out.println("请输入模块名称：");
        Scanner input=new Scanner(System.in);
        String modelName=input.next();
        input.close();
        System.out.println("-------开始生成-------");
        AutoGenerator generator = new AutoGenerator();
        generator.setGlobalConfig(getGlobalConfig());
        generator.setDataSource(getDataSourceConfig());
        generator.setStrategy(getStrategyConfig(modelName));
        generator.setPackageInfo(getPackageConfig(modelName));
        generator.execute();
    }

    private static GlobalConfig getGlobalConfig(){
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir("D://generator/src/main/java");
        globalConfig.setAuthor("zhangzhiwei");
        globalConfig.setOpen(false);
        globalConfig.setKotlin(false);
        globalConfig.setSwagger2(true);
        globalConfig.setFileOverride(true);
        globalConfig.setDateType(DateType.ONLY_DATE);
        return globalConfig;
    }

    private static DataSourceConfig getDataSourceConfig(){
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://127.0.0.1:3306/xxx?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&serverTimezone=Asia/Shanghai");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("password");

        return dataSourceConfig;
    }

    private static StrategyConfig getStrategyConfig(String modelName){
        StrategyConfig strategyConfig=new StrategyConfig();
        strategyConfig.setTablePrefix("wk_");
        strategyConfig.setLikeTable(new LikeTable("wk_"+modelName, SqlLike.RIGHT));
        strategyConfig.setRestControllerStyle(true);
        strategyConfig.setSkipView(true);
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setEntityLombokModel(true);
        List<TableFill> tableFillList=new ArrayList<>();
        tableFillList.add(new TableFill("create_time", FieldFill.INSERT));
        tableFillList.add(new TableFill("update_time", FieldFill.UPDATE));
        tableFillList.add(new TableFill("create_user_id", FieldFill.INSERT));
        strategyConfig.setTableFillList(tableFillList);
        strategyConfig.setSuperMapperClass("com.kakarote.core.servlet.BaseMapper");
        strategyConfig.setSuperServiceClass("com.kakarote.core.servlet.BaseService");
        strategyConfig.setSuperServiceImplClass("com.kakarote.core.servlet.BaseServiceImpl");
        return strategyConfig;
    }
    private static PackageConfig getPackageConfig(String modelName){
        PackageConfig packageConfig=new PackageConfig();
        packageConfig.setParent("com.kakarote."+modelName);
        packageConfig.setEntity("entity.PO");
        return packageConfig;
    }
}
