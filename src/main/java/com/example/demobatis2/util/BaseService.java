package com.example.demobatis2.util;

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
