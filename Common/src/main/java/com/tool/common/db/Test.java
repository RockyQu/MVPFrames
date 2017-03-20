package com.tool.common.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by SZLD-PC-249 on 2017/3/10.
 */
@Entity
public class Test {
    
    int id;

    @Generated(hash = 442027691)
    public Test(int id) {
        this.id = id;
    }

    @Generated(hash = 372557997)
    public Test() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
