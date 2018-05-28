package com.dyingbleed.corgi.spark.core

import com.alibaba.fastjson.JSON
import com.google.common.base.Charsets
import com.google.inject.Inject
import com.google.inject.name.Named
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

/**
  * Created by 李震 on 2018/3/1.
  */
class Conf @Inject()(@Named("appName") appName: String, @Named("apiServer") apiServer: String) {

  private val jobConf = JSON.parseObject(queryBatchTaskApi())

  private val cacheConf = JSON.parseObject("")

  private def queryBatchTaskApi(): String = {
    val httpClient = HttpClients.createDefault()
    val httpGet = new HttpGet("http://" + apiServer + "/api/conf?name=" + appName)
    val httpResponse = httpClient.execute(httpGet)

    val statusCode = httpResponse.getStatusLine.getStatusCode
    if (statusCode >= 200 && statusCode < 300) {
      val entity = httpResponse.getEntity
      val content = EntityUtils.toString(entity, Charsets.UTF_8)
      httpClient.close()
      content
    } else {
      httpClient.close()
      throw new RuntimeException("调用批处理任务接口返回 " + statusCode)
    }
  }

  def mode: ODSMode = ODSMode.valueOf(jobConf.getString("mode"))

  def modifyTimeColumn: String = jobConf.getString("timeColumn")

  def dbUrl: String = jobConf.getString("dataSourceUrl")

  def dbTable: String = jobConf.getString("sourceTable")

  def dbUser: String = jobConf.getString("dataSourceUsername")

  def dbPassword: String = jobConf.getString("dataSourcePassword")

  def hiveDB: String = jobConf.getString("sinkDb")

  def hiveTable: String = jobConf.getString("sinkTable")

}
