package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        //数据库版本号；自动生成的bean对象放到 /java-gen/[PackageDir]bean下
        Schema schema = new Schema(1, "com.tongban.im.db.bean");
        //自动生成的dao对象放到 /java-gen/[PackageDir]dao下
        schema.setDefaultJavaPackageDao("com.tongban.im.db.dao");
        //初始化bean
        initUserTable(schema);
        initGroupTable(schema);
        //开始创建
        new DaoGenerator().generateAll(schema, args[0]);
    }

    private static void initUserTable(Schema schema) {
        //表名称
        Entity userTable = schema.addEntity("UserTable");
        // 主键，索引
        userTable.addStringProperty("user_id").primaryKey().index();
        userTable.addStringProperty("mobile_phone");
        userTable.addStringProperty("nick_name");
        userTable.addStringProperty("portrait_url");
        userTable.addStringProperty("tags");
        userTable.addStringProperty("declaration");

    }

    private static void initGroupTable(Schema schema) {
        //表名称
        Entity groupTable = schema.addEntity("GroupTable");
        // 主键，索引
        groupTable.addStringProperty("group_id").primaryKey().index();
        groupTable.addStringProperty("group_name");
        groupTable.addStringProperty("group_avatar");

    }


}
