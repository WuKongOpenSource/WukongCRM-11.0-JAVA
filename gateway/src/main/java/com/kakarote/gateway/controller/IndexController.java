package com.kakarote.gateway.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.ProjectVersionEntity;
import com.kakarote.core.entity.UpdateConfigEntity;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.projectupdate.UpdateProjectVersionUtil;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.gateway.service.ProjectUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.sql.DataSource;
import java.net.URI;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author Administrator
 */
@RestController
public class IndexController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProjectUpdateService projectUpdateService;

    @RequestMapping("/")
    public Mono<Void> index(ServerHttpResponse response) {
        return Mono.fromRunnable(() -> {
            response.setStatusCode(HttpStatus.FOUND);
            response.getHeaders().setLocation(URI.create("./index.html"));
        });
    }

    @RequestMapping("/updates/login")
    public JSONObject login(@RequestBody JSONObject jsonObject) {
        HttpRequest httpRequest = HttpUtil.createPost("https://center.72crm.com/updates/login");
        httpRequest.form("username", jsonObject.getString("username"));
        httpRequest.form("password", jsonObject.getString("password"));
        HttpResponse execute = httpRequest.execute();
        return JSONObject.parseObject(execute.body());
    }

    @RequestMapping("/ping")
    public Result ping() {
        return Result.ok();
    }

    @PostMapping("/checkVersion")
    public Result<JSONObject> checkVersion() {
        JSONObject jsonObject = new JSONObject();
        ProjectVersionEntity projectVersionEntity = UpdateProjectVersionUtil.checkVersion();
        jsonObject.put("version", Const.PROJECT_VERSION);
        if (projectVersionEntity != null) {
            jsonObject.put("serverVersion", projectVersionEntity.getVersion());
        } else {
            jsonObject.put("serverVersion", Const.PROJECT_VERSION);
        }
        return Result.ok(jsonObject);
    }

    @PostMapping("/getProgress")
    public Result<Integer> getProgress() {
        Integer progress = UpdateProjectVersionUtil.getProgress();
        if (Objects.equals(-1, progress)) {
            UpdateProjectVersionUtil.setProgress(null);
            throw new CrmException(UpdateProjectVersionUtil.getCodeEnum());
        }
        if (Objects.equals(100, progress)) {
            UpdateProjectVersionUtil.setProgress(null);
        }
        return R.ok(progress);
    }

    @PostMapping("/queryDatabase")
    public Result queryDatabase() {
        ProjectVersionEntity projectVersionEntity = UpdateProjectVersionUtil.checkVersion();
        if (projectVersionEntity != null) {
            List<String> databaseList = new ArrayList<>();
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData dmd = connection.getMetaData();
                ResultSet rs = dmd.getCatalogs();
                while (rs.next()) {
                    String table_cat = rs.getString("TABLE_CAT");
                    databaseList.add(table_cat);
                }
            } catch (Exception ignored) {

            }
            return R.ok(databaseList);
        }
        return R.ok();
    }

    @PostMapping("/backupDatabase")
    public Result<String> backupDatabase(@RequestBody UpdateConfigEntity entity) {
        entity.setBackupPath(FileUtil.getParent(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), 2));
        String backupSql = UpdateProjectVersionUtil.backupSql(entity);
        return R.ok(FileUtil.normalize(backupSql));
    }


    @PostMapping("/projectUpdate")
    public Result projectUpdate(@RequestBody UpdateConfigEntity entity) {
        UpdateProjectVersionUtil.setProgress(0);
        TaskExecutor taskExecutor = ApplicationContextHolder.getBean(TaskExecutor.class);
        taskExecutor.execute(() -> projectUpdateService.updateVersion(entity));
        return R.ok();
    }
}
