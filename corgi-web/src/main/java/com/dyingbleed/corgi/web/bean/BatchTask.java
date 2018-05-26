package com.dyingbleed.corgi.web.bean;

/**
 * 批处理任务 POJO
 *
 * Created by 李震 on 2018/5/15.
 */
public class BatchTask {

    private Long id;

    private String name;

    private Long datasource_id;

    private String datasource_url;

    private String datasource_username;

    private String datasource_password;

    private String source_db;

    private String source_table;

    private String mode;

    private String time_column;

    private String sink_db;

    private String sink_table;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDatasource_id() {
        return datasource_id;
    }

    public void setDatasource_id(Long datasource_id) {
        this.datasource_id = datasource_id;
    }

    public String getDatasource_url() {
        return datasource_url;
    }

    public void setDatasource_url(String datasource_url) {
        this.datasource_url = datasource_url;
    }

    public String getDatasource_username() {
        return datasource_username;
    }

    public void setDatasource_username(String datasource_username) {
        this.datasource_username = datasource_username;
    }

    public String getDatasource_password() {
        return datasource_password;
    }

    public void setDatasource_password(String datasource_password) {
        this.datasource_password = datasource_password;
    }

    public String getSource_db() {
        return source_db;
    }

    public void setSource_db(String source_db) {
        this.source_db = source_db;
    }

    public String getSource_table() {
        return source_table;
    }

    public void setSource_table(String source_table) {
        this.source_table = source_table;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTime_column() {
        return time_column;
    }

    public void setTime_column(String time_column) {
        this.time_column = time_column;
    }

    public String getSink_db() {
        return sink_db;
    }

    public void setSink_db(String sink_db) {
        this.sink_db = sink_db;
    }

    public String getSink_table() {
        return sink_table;
    }

    public void setSink_table(String sink_table) {
        this.sink_table = sink_table;
    }

    @Override
    public String toString() {
        return "BatchTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", datasource_id=" + datasource_id +
                ", source_db='" + source_db + '\'' +
                ", source_table='" + source_table + '\'' +
                ", mode='" + mode + '\'' +
                ", sink_db='" + sink_db + '\'' +
                ", sink_table='" + sink_table + '\'' +
                '}';
    }
}