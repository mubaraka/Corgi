package com.dyingbleed.corgi.spark.util

import java.sql.{Connection, DriverManager, ResultSet, Timestamp}

import com.dyingbleed.corgi.spark.bean.{Column, ColumnStat}
import org.joda.time.LocalDateTime

import scala.collection.mutable.ListBuffer

/**
  * Created by 李震 on 2018/9/27.
  */
object JDBCUtils {

  def getConnection(url: String, username: String, password: String): Connection = {
    if (url.startsWith("jdbc:mysql")) {
      Class.forName("com.mysql.jdbc.Driver")
    } else if(url.startsWith("jdbc:oracle:thin")) {
      Class.forName("oracle.jdbc.OracleDriver")
    } else {
      throw new RuntimeException("不支持的数据源")
    }
    DriverManager.getConnection(url, username, password)
  }

  /**
    * 获取主键信息
    *
    * @param conn 数据库连接
    * @param db 数据库名
    * @param table 表名
    *
    * @return 主键信息
    * */
  def getPrimaryKey(conn: Connection, db: String, table: String): Seq[Column] = {
    val pks = new ListBuffer[Column]

    val dbmd = conn.getMetaData
    val pkrs = dbmd.getPrimaryKeys(db, null, table)

    while (pkrs.next()) {
      val primaryKeyColumn = pkrs.getString(4)

      val crs = dbmd.getColumns(db, null, table, primaryKeyColumn)
      if (crs.next()) {
        val dataType = crs.getInt(5)
        pks += Column(primaryKeyColumn, dataType)
      }
      crs.close()
    }
    pkrs.close()

    pks
  }

  /**
    * 获取列信息
    *
    * @param conn 数据库连接
    * @param db 数据库名
    * @param table 表名
    *
    * @return 列信息
    * */
  def getColumns(conn: Connection, db: String, table: String): Seq[Column] = {
    val cs = new ListBuffer[Column]

    val metaData = conn.getMetaData
    val crs = metaData.getColumns(db, db, table, null)

    while (crs.next()) {
      val cName = crs.getString(4)
      val ctype = crs.getInt(5)
      cs += Column(cName, ctype)
    }
    crs.close()

    cs
  }

  /**
    * 获取列统计信息
    *
    * @param conn 数据库连接
    * @param db 数据库名
    * @param table 表名
    *
    * @return 列统计信息
    * */
  def getColumnStat(conn: Connection, db: String, table: String, column: String): ColumnStat = {
    val sql = s"""
         |SELECT
         |  MAX($column) AS max,
         |  MIN($column) AS min,
         |  COUNT(1) AS count
         |FROM $db.$table
      """.stripMargin

    val r = getOne(conn, sql, rs => {
      val max = rs.getObject(1)
      val min = rs.getObject(2)
      val count = rs.getLong(3)
      ColumnStat(max, min, count)
    })

    r
  }

  /**
    * 获取最大值
    *
    * @param conn 数据库连接
    * @param db 数据库名
    * @param table 表名
    *
    * @return 表基数信息
    * */
  def getColumnMax(conn: Connection, db: String, table: String, column: String): Any = {
    val sql =
      s"""
         |SELECT
         |  MAX($column) AS max
         |FROM $db.$table
         |WHERE $column IS NOT NULL
      """.stripMargin

    val r = getOne(conn, sql, rs => rs.getObject(1))

    r
  }

  /**
    * 获取最小值
    *
    * @param conn 数据库连接
    * @param db 数据库名
    * @param table 表名
    *
    * @return 表基数信息
    * */
  def getColumnMin(conn: Connection, db: String, table: String, column: String): Any = {
    val sql =
      s"""
         |SELECT
         |  MIN($column) AS min
         |FROM $db.$table
         |WHERE $column IS NOT NULL
      """.stripMargin

    val r = getOne(conn, sql, rs => rs.getObject(1))

    r
  }

  /**
    * 获取表基数信息
    *
    * @param conn 数据库连接
    * @param db 数据库名
    * @param table 表名
    *
    * @return 表基数信息
    * */
  def getCardinality(conn: Connection, db: String, table: String): Long = {
    val sql =
      s"""
         |SELECT
         |  COUNT(1) AS count
         |FROM $db.$table
      """.stripMargin

    val r = getOne(conn, sql, rs => rs.getLong(1))

    r
  }

  /**
    * 获取表字段所有值
    *
    * @param conn 数据库连接
    * @param db 数据库名
    * @param table 表名
    * @param columnName 字段名
    *
    * @return 表字段所有值
    * */
  def getDistinct(conn: Connection, db: String, table: String, columnName: String): Seq[Any] = {
    val sql =
      s"""
        |SELECT
        |  DISTINCT $columnName
        |FROM $db.$table
      """.stripMargin

    val r = getMany(conn, sql, rs => rs.getObject(1))

    r
  }

  /**
    * 获取表字段所有值
    *
    * @param conn 数据库连接
    * @param db 数据库名
    * @param table 表名
    * @param columnName 字段名
    * @param tsColumnName 时间戳字段名
    * @param beginTime 开始时间
    * @param endTime 结束时间
    *
    * @return 表字段所有值
    * */
  def getDistinct(conn: Connection, db: String, table: String, columnName: String, tsColumnName: String, beginTime: LocalDateTime, endTime: LocalDateTime): Seq[Any] = {
    val sql =
      s"""
         |SELECT
         |  DISTINCT $columnName
         |FROM $db.$table
         |WHERE $tsColumnName > ?
         |AND $tsColumnName < ?
      """.stripMargin

    val stat = conn.prepareStatement(sql)
    stat.setTimestamp(1, new Timestamp(beginTime.toDateTime.getMillis))
    stat.setTimestamp(2, new Timestamp(endTime.toDateTime.getMillis))
    val rs = stat.executeQuery()

    val buf = new ListBuffer[Any]()

    while(rs.next()) {
      val r = rs.getObject(1)
      buf += r
    }

    rs.close()
    stat.close()

    buf
  }

  private def getOne[T](conn: Connection, sql: String, rsHandler: ResultSet => T): T = {
    val stat = conn.createStatement()
    val rs = stat.executeQuery(sql)
    rs.next()
    val r = rsHandler(rs)
    rs.close()
    stat.close()
    r
  }

  private def getMany[T](conn: Connection, sql: String, rsHandler: ResultSet => T): Seq[T] = {
    val stat = conn.createStatement()
    val rs = stat.executeQuery(sql)

    val buf = new ListBuffer[T]()

    while(rs.next()) {
      val r = rsHandler(rs)
      buf += r
    }

    rs.close()
    stat.close()

    buf
  }

}
