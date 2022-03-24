package com.lijiahao.chargingpilebackend;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

//https://juejin.cn/post/7049272958790926343

public class MybatisPlusGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/chargingpile?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=UTC&useSSL=false", "root", "asdHAO0624770")
                .globalConfig(builder -> {
                    builder.author("lijiahao") // 设置作者
                            // 设置输出路径
                            .outputDir(System.getProperty("user.dir") + "/src/main/java")
                            .commentDate("yyyy-MM-dd")
                            .dateType(DateType.TIME_PACK)
                            .enableSwagger() // 开启Swagger
                            .build();
                })
                .packageConfig(builder -> {
                    builder.parent("com.lijiahao.chargingpilebackend") // 设置包路径
                            .entity("entity") // 设置实体包名
                            .service("service") // 设置service包名
                            .serviceImpl("service.impl") // 设置service实现包名
                            .mapper("mapper") // 设置mapper包名
                            .controller("controller") // 设置controller包名
                            .other("configuration"); // 设置其他包名


                })
                .strategyConfig(builder -> {
                    builder
//                            .addInclude("message") // 设置表名
                            .enableSkipView()
                            .entityBuilder()
                            .idType(IdType.AUTO);
                })
                .execute();
    }
}
