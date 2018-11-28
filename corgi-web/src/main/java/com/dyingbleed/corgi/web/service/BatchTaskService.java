package com.dyingbleed.corgi.web.service;

import com.dyingbleed.corgi.web.bean.BatchTask;
import com.dyingbleed.corgi.web.mapper.BatchTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 批处理任务
 *
 * Created by 李震 on 2018/5/15.
 */
@Service
public class BatchTaskService {

    @Autowired
    private BatchTaskMapper batchTaskMapper;

    /**
     * 新增批量任务
     *
     * @param task 批量任务
     *
     * @return 批量任务
     *
     * */
    public BatchTask insertBatchTask(BatchTask task) {
        this.batchTaskMapper.insertBatchTask(task);
        return task;
    }

    /**
     * 根据 ID 删除批量任务
     *
     * @param id 批量任务 ID
     *
     * */
    public void deleteBatchTaskById(Long id) {
        this.batchTaskMapper.deleteBatchTaskById(id);
    }

    /**
     * 修改批量任务
     *
     * @param task 批量任务
     *
     * @return 批量任务
     *
     * */
    public BatchTask updateBatchTask(BatchTask task) {
        this.batchTaskMapper.updateBatchTask(task);
        return task;
    }

    /**
     * 查询所有批量任务
     *
     * @return 批量任务列表
     *
     * */
    public List<BatchTask> queryAllBatchTask() {
        return this.batchTaskMapper.queryAllBatchTask();
    }

    /**
     * 根据 ID 查询批量任务
     *
     * @param id 批量任务 ID
     *
     * @return 批量任务
     *
     * */
    public BatchTask queryBatchTaskById(Long id) {
        return this.batchTaskMapper.queryBatchTaskById(id);
    }

    /**
     * 根据名称查询批量任务
     *
     * @param name 批量任务名称
     *
     * @return 批量任务
     *
     * */
    public BatchTask queryBatchTaskByName(String name) {
        return this.batchTaskMapper.queryBatchTaskByName(name);
    }

}
