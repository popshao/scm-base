package com.gangling.scm.base.middleware.email;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public abstract class MongodbBaseDAO<T> {

	@Autowired
	protected MongoTemplate mongoTemplate;


	/**
	 * 通过条件查询,查询分页结果
	 */
	public PageInfo<T> findPg(int pageNo, int pageSize, Query query, String collectionName) {
		long totalCount = this.mongoTemplate.count(query, this.getEntityClass());
		PageInfo<T> page = new PageInfo<T>();
		int start = Math.max(0, (pageNo - 1)) * pageSize;
		query.skip(start);
		query.limit(pageSize);
		List<T> datas = this.find(query, collectionName);
		page.setPageSize(pageSize);
		page.setPageNum(pageNo);
		page.setTotal((int)totalCount);
		page.setList(datas);
		return page;
	}

	/**
	 * 通过条件查询实体(集合)
	 */
	public List<T> find(Query query, String collectionName) {
		return mongoTemplate.find(query, this.getEntityClass(), collectionName);
	}

	/**
	 * 保存一个对象到mongodb
	 */
	public T save(T bean) {
		mongoTemplate.save(bean);
		return bean;
	}

	/**
	 * 保存一个对象到mongodb
	 */
	public T save(T bean, String collectionName) {
		mongoTemplate.save(bean, collectionName);
		return bean;
	}

	public void batchInsert(List<T> list, String collectionName) {
		mongoTemplate.insert(list, collectionName);
	}

	/**
	 * 获取需要操作的实体类class
	 */
	protected abstract Class<T> getEntityClass();

	protected abstract void setMongoTemplate(MongoTemplate mongoTemplate);

}