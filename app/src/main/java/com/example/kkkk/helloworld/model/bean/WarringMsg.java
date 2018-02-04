package com.example.kkkk.helloworld.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/5/3.
 * 位置信息
 */

public class WarringMsg implements Serializable {

    /**
     * uuid : c03babe6-6ad7-4deb-af67-cae064767948
     * title : 测试公告
     * content : 不晓得啦阿达女大多数大
     * createTime : 2017-10-27 09:43:02
     * urgency : 1
     * status : 1
     * createUser : {"uuid":"b18fea7c-6090-447d-9794-1b00b69f22d5","username":"admin","mobile":"admin","nickname":"系统管理员"}
     */

    private String uuid;
    private String title;
    private String content;
    private String createTime;
    private int urgency;
    private int status;
    private CreateUserBean createUser;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CreateUserBean getCreateUser() {
        return createUser;
    }

    public void setCreateUser(CreateUserBean createUser) {
        this.createUser = createUser;
    }

    public static class CreateUserBean {
        /**
         * uuid : b18fea7c-6090-447d-9794-1b00b69f22d5
         * username : admin
         * mobile : admin
         * nickname : 系统管理员
         */

        private String uuid;
        private String username;
        private String mobile;
        private String nickname;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
