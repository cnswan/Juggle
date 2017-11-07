package com.cnswan.juggle.bean;

/**
 * Created by 00013259 on 2017/9/30.
 */

public class Library extends BaseBean {

    private String name;
    private String desc;

    public Library(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Library library = (Library) o;

        if (name != null ? !name.equals(library.name) : library.name != null)
            return false;
        return desc != null ? desc.equals(library.desc) : library.desc == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        return result;
    }
}