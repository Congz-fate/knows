package com.zc.knowsportal.vo;

import lombok.Data;

import java.util.Objects;

/**
 * @Author Cong
 * @ClassName Massage
 * @Description 反馈信息
 * @Date 15/11/2022  下午 4:10
 */
@Data
public class Massage {
    private Integer id ;
    private String name ;
    private String content ;

    public Massage() {
    }

    public Massage(Integer id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Massage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Massage massage = (Massage) o;
        return Objects.equals(id, massage.id) && Objects.equals(name, massage.name) && Objects.equals(content, massage.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, content);
    }
}
