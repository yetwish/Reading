package com.aidingmao.database;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class DatabaseGenerator {

	public static final int DATABASE_VERSION = 1;

	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(DATABASE_VERSION,
				"com.xidian.yetwish.reading.framework.database.generator");

		addDepartment(schema);
		addUser(schema);
		addGroupMember(schema);
		addSchedule(schema);
		new DaoGenerator().generateAll(schema, "../reading/src/main/java");
	}

	private static void addSchedule(Schema schema) {
		Entity schedule = schema.addEntity("Schedule");
		schedule.addLongProperty("sid").primaryKey();
		schedule.addLongProperty("uid");
		schedule.addIntProperty("type");
		schedule.addStringProperty("content");
		schedule.addLongProperty("scheduleTime");
		schedule.addStringProperty("createTime");
		schedule.addStringProperty("hostName");
		schedule.addIntProperty("alertTime");
		schedule.addIntProperty("alertType");
		schedule.addIntProperty("priority");
		schedule.addStringProperty("mediaLocalPath");
		schedule.addStringProperty("attachmentDigest");
	}

	private static void addGroupMember(Schema schema) {
		Entity conver = schema.addEntity("GroupMember");
		conver.addIdProperty().autoincrement();
		Property cid = conver.addLongProperty("cid").getProperty();
		Property uid = conver.addStringProperty("uid").getProperty();
		conver.addStringProperty("userName");

		Index index = new Index();
		index.addProperty(uid);
		index.addProperty(cid);
		index.makeUnique();
		conver.addIndex(index);
	}

	private static void addDepartment(Schema schema) {
		Entity depart = schema.addEntity("Department");
		depart.addIdProperty().autoincrement();
		Property orgId = depart.addLongProperty("orgId").getProperty();
		Property departId = depart.addLongProperty("departmentId")
				.getProperty();
		Property parentId = depart.addLongProperty("parentId").getProperty();
		depart.addLongProperty("userCounts");
		depart.addIntProperty("sequence");
		depart.addStringProperty("name");
		depart.addStringProperty("description");
		depart.addStringProperty("parentIds");
		depart.addStringProperty("orgName");

		Index index = new Index();
		index.addProperty(departId);
		index.addProperty(orgId);
		index.makeUnique();
		depart.addIndex(index);

		Index orgIdIndex = new Index();
		orgIdIndex.addProperty(orgId);
		depart.addIndex(orgIdIndex);

		Index parentIdIndex = new Index();
		parentIdIndex.addProperty(parentId);
		parentIdIndex.addProperty(orgId);
		depart.addIndex(parentIdIndex);
	}

	private static void addUser(Schema schema) {
		Entity user = schema.addEntity("User");
		user.addIdProperty().autoincrement();
		Property uid = user.addLongProperty("uid").getProperty();
		Property orgId = user.addLongProperty("orgId").getProperty();
		Property departId = user.addLongProperty("departmentId").getProperty();
		user.addIntProperty("sequence");
		Property mobile = user.addStringProperty("mobile").getProperty();
		user.addStringProperty("title");
		Property name = user.addStringProperty("name").getProperty();
		user.addStringProperty("pinyin");
		user.addIntProperty("sex");
		user.addStringProperty("email");
		user.addStringProperty("homePhone");
		user.addStringProperty("personalCellPhone");
		user.addStringProperty("shortNum");
		user.addStringProperty("shortNum2");
		user.addStringProperty("workPhone");
		user.addStringProperty("workPhone2");
		user.addStringProperty("virtualCellPhone");
		user.addStringProperty("remark");
		user.addBooleanProperty("isAllowLogin");
		user.addStringProperty("virtualCode");
		user.addStringProperty("fax");
		user.addStringProperty("shortPinyin");
		user.addStringProperty("customField");
		user.addStringProperty("privilege");
		user.addStringProperty("orgName");

		Index index = new Index();
		index.addProperty(uid);
		index.addProperty(departId);
		index.addProperty(orgId);
		index.makeUnique();
		user.addIndex(index);

		Index orgIdIndex = new Index();
		orgIdIndex.addProperty(orgId);
		user.addIndex(orgIdIndex);

		Index nameIndex = new Index();
		nameIndex.addProperty(name);
		user.addIndex(nameIndex);

		Index mobileIndex = new Index();
		mobileIndex.addProperty(mobile);
		user.addIndex(mobileIndex);

		Index uidIndex = new Index();
		uidIndex.addProperty(uid);
		user.addIndex(uidIndex);

		Index departIndex = new Index();
		departIndex.addProperty(departId);
		departIndex.addProperty(orgId);
		user.addIndex(departIndex);

		Index groupIndex = new Index();
		groupIndex.addProperty(uid);
		groupIndex.addProperty(orgId);
		user.addIndex(groupIndex);
	}

}
