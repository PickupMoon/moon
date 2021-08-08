import org.dounana.core.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class JdbcTemplateTest {

    JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate();
    }


    @Test
    public void queryList() {
        String sql = "select * from user";
        RowMapper<Map<String, Object>> rowMapper = new MapRowMapper();
        List<Map<String, Object>> mapList = jdbcTemplate.queryList(sql, rowMapper);
        System.out.println(mapList);
        Assert.assertNotNull(mapList);
    }

    @Test
    public void queryListByParameter() {
        String sql = "select * from user where  id >= ? ";
        RowMapper<Map<String,Object>> rowMapper = new MapRowMapper();
        Object[] args = new Object[]{2};
        List<Map<String, Object>> mapList = jdbcTemplate.queryList(sql, rowMapper, args);
        System.out.println(mapList);
        Assert.assertNotNull(mapList);
    }

    @Test
    public void queryListByType() {
        String sql = "select * from user";
        List<User> mapList = jdbcTemplate.queryList(sql, User.class);
        System.out.println(mapList);
        Assert.assertNotNull(mapList);
    }

    @Test(expected = RuntimeException.class)
    public void queryObject(){
        //1. parepare data
        //2 execute
        //3. compare
        String sql = "select * from user where id >=1 ";
        Map<String, Object> stringObjectMap = jdbcTemplate.queryObject(sql, new MapRowMapper());
    }

    @Test
    public void queryObjectByArgs() {
        String sql = "select * from user where name = ?";
        Map<String, Object> stringObjectMap = jdbcTemplate.queryObject(sql, new MapRowMapper(), new Object[]{"xiaoXuanXuan"});
        System.out.println(stringObjectMap);
        Assert.assertNotNull(stringObjectMap);
    }

    @Test
    public void queryObjectByTargetClass() {
        User user = jdbcTemplate.queryObject("select * from user where id = 1", User.class);
        System.out.println(user);
        Assert.assertNotNull(user);
    }

    @Test
    public void executeUpdate() {

        User userUpdateBefore = jdbcTemplate.queryObject("select * from user where id=3", User.class);
        System.out.println(userUpdateBefore);
        Assert.assertNotNull(userUpdateBefore);

        String sql = "update user set name = 'xiaoXiaoNa' where name='xiaoXiaoXuan'";
        int rowCounts = jdbcTemplate.executeUpdate(sql);
        System.out.println(rowCounts);
        User userUpdateAfter = jdbcTemplate.queryObject("select * from user where id=3", User.class);
        System.out.println(userUpdateAfter);
        Assert.assertNotNull(userUpdateAfter);
    }

    @Test
    public void executeUpdateByArgs() {
        String sql = "update user set name = ? where id = ?";
        Object[] args = new Object[]{"zhaoHongXuan",2};

        String selectSql = "select * from user where id =2";
        User userUpdateBefore = jdbcTemplate.queryObject(selectSql, User.class);
        System.out.println("Message for update before [ "+userUpdateBefore+" ]");
        Assert.assertNotNull(userUpdateBefore);

        int rowCounts = jdbcTemplate.executeUpdate(sql, args);

        System.out.println("影响的行数:[ "+rowCounts+" ]");

        User userUpdateAfter = jdbcTemplate.queryObject(selectSql, User.class);
        System.out.println("Message for update after [ "+userUpdateAfter+" ]");
        Assert.assertNotNull(userUpdateAfter);
    }

    @Test
    public void executeUpdateForCreateTable() {
        String sql = "create table branch (branch_No int , branch_name varchar(64))";
        jdbcTemplate.execute(sql);
    }
}
