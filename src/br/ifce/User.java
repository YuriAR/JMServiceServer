package br.ifce;

import java.util.List;

/**
 * Created by yurir on 28/02/2017.
 */
public class User {
    public String userName;
    public String queueName;

    public User(String name){
        this.userName = name;
        this.queueName = "queue" + userName;
    }

    public Boolean userExists(List<User> users){
        if (users.contains(this)){
            return  true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;
        return queueName != null ? queueName.equals(user.queueName) : user.queueName == null;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (queueName != null ? queueName.hashCode() : 0);
        return result;
    }
}
