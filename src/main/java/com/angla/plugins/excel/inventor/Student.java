package com.angla.plugins.excel.inventor;

import com.angla.plugins.excel.inventor.anno.InventorField;

import java.util.Date;

/**
 * Title:Student
 *
 * @author liumenghua
 **/
public class Student {


    @InventorField(name = "姓名")
    private String name;

    @InventorField(name = "年龄")
    private Integer age;

    @InventorField(name = "性别")
    private String sex;

    @InventorField(name = "生日")
    private Date birth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", birth=" + birth +
                '}';
    }
}