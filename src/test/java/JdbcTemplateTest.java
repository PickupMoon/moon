import org.dounana.core.*;
import org.dounana.entity.User;
import org.dounana.utils.JdbcUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcTemplateTest {

    JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(JdbcUtil.connection());
        String dropSql = "drop table if exists user";
        jdbcTemplate.execute(dropSql);
        String createSql= "create TABLE USER (id int PRIMARY KEY,name VARCHAR(64),age int,create_time  datetime DEFAULT CURRENT_TIMESTAMP)";
        jdbcTemplate.execute(createSql);
    }




    public void executeUpdate() {
        String sql2 = "insert into user (id,name,age) values (1,'zhaohongxuan',28)" ;
        int rowCounts2 = jdbcTemplate.executeUpdate(sql2);
        Assert.assertEquals(1,rowCounts2);

    }

    @Test
    public void queryList() {
        executeUpdate();
        String sql = "select * from user";
        RowMapper<Map<String, Object>> rowMapper = new MapRowMapper();
        List<Map<String, Object>> mapList = jdbcTemplate.queryList(sql, rowMapper);
        System.out.println(mapList);
        Assert.assertNotNull(mapList);
    }

    @Test
    public void queryListByParameter() {
        executeUpdate();
        String sql = "select * from user where  id >= ? ";
        RowMapper<Map<String,Object>> rowMapper = new MapRowMapper();
        List<Map<String, Object>> mapList = jdbcTemplate.queryList(sql, rowMapper, 1);
        System.out.println(mapList);
        Assert.assertEquals(1,mapList.size());
    }

    @Test
    public void queryListByType() {
        executeUpdate();
        String sql = "select * from user";
        List<User> mapList = jdbcTemplate.queryList(sql, User.class);
        System.out.println(mapList);
        Assert.assertEquals(1,mapList.size());
    }

    @Test
    public void queryObject(){
        //1. parepare data
        //2 execute
        //3. compare
        executeUpdate();
        String sql = "select * from user where id = 1 ";
        Map<String, Object> stringObjectMap = jdbcTemplate.queryObject(sql, new MapRowMapper());
        System.out.println(stringObjectMap);
        Assert.assertEquals(1,stringObjectMap.get("id"));
    }

    @Test
    public void queryObjectByArgs() {
        executeUpdate();
        String sql = "select * from user where name = ?";
        Map<String, Object> stringObjectMap = jdbcTemplate.queryObject(sql, new MapRowMapper(), "zhaohongxuan");
        System.out.println(stringObjectMap);
        Assert.assertEquals("zhaohongxuan",stringObjectMap.get("name"));
    }

    @Test
    public void queryObjectByTargetClass() {
        executeUpdate();
        User user = jdbcTemplate.queryObject("select * from user where id = 1", User.class);
        System.out.println(user);
        Assert.assertEquals(1,user.getId());
    }



    @Test
    public void executeUpdateByArgs() {
        executeUpdate();
        String sql = "update user set name = ? where id = ?";
        int rowCounts = jdbcTemplate.executeUpdate(sql, "xiaoxuanxuan",1);
        Assert.assertEquals(1,rowCounts);
    }

    @Test
    public void testExecuteBatchUpdate(){
        String sql = "insert into user (id,name,age) values (?,?,?)" ;
        List<Object[]> argsList = new ArrayList<>();
        argsList.add(new Object[]{1,"zhaohong",29});
        argsList.add(new Object[]{2,"zhaohongxuan",29});
        argsList.add(new Object[]{3,"xiaoxiaoxuan",1});
        argsList.add(new Object[]{4,"xiaoxiaona",0});
        argsList.add(new Object[]{5,"dounana",32});
        int[] batchUpdateArray = jdbcTemplate.executeBatchUpdate(sql, argsList);
        int[] expectArray = new int[]{1,1,1,1};
        Assert.assertArrayEquals(expectArray,batchUpdateArray);

        queryList();
    }

    @Test
    public void testQueryObject(){
        testExecuteBatchUpdate();
        String sql = "select * from user where id = ?";
        User user = jdbcTemplate.queryObject(sql, User.class, 1);
        System.out.println(user);
        Assert.assertEquals(1,user.getId());

    }

}
