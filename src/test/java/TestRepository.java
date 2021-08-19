import com.sun.xml.internal.bind.v2.model.core.ID;
import org.dounana.core.JdbcOperation;
import org.dounana.core.JdbcTemplate;
import org.dounana.entity.User;
import org.dounana.orm.BaseRepository;
import org.dounana.orm.Repository;
import org.dounana.orm.SqlMapper;
import org.dounana.utils.JdbcUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class TestRepository {

    private JdbcOperation jdbcOperation;

    Logger logger = LoggerFactory.getLogger(TestRepository.class);

    @Before
    public void setUp() {
        jdbcOperation = new JdbcTemplate(JdbcUtil.connection());
        String dropSql = "drop table if exists user";
        jdbcOperation.execute(dropSql);
        String createSql= "create TABLE USER (id int PRIMARY KEY,name VARCHAR(64),age int,create_time  datetime DEFAULT CURRENT_TIMESTAMP)";
        jdbcOperation.execute(createSql);
    }


    @Test
    public void testRepositoryFindById() {
        String sql2 = "insert into user (id,name,age) values (1,'zhaohongxuan',28)" ;
        int rowCounts2 = jdbcOperation.executeUpdate(sql2);
        Assert.assertEquals(1,rowCounts2);

        Repository<User, String> repository = new BaseRepository<>(jdbcOperation, User.class);
        String id = "1";
        User user = repository.findById(id);
        Assert.assertEquals(1,user.getId());
    }

    @Test
    public void testSelectAll() {
        String sql1 = "insert into user (id,name,age) values (1,'zhaohongxuan',28)" ;
        String sql2 = "insert into user (id,name,age) values (2,'dounana',28)" ;
        int rowCounts1 = jdbcOperation.executeUpdate(sql1);
        int rowCounts2 = jdbcOperation.executeUpdate(sql2);

        Assert.assertEquals(1,rowCounts2);

        Repository<User, String> repository = new BaseRepository<>(jdbcOperation, User.class);
        List<User> users = repository.findAll();
        logger.info(users.toString());
        Assert.assertNotNull(users);
    }

    @Test
    public void testFor() {
        SqlMapper<User> sqlMapper = new SqlMapper<>(User.class);
        List<String> tableColumns = sqlMapper.getTableColumns(User.class);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tableColumns.size(); i++) {
            if (i != tableColumns.size()-1) {
                stringBuilder.append(tableColumns.get(i)).append(",");
            } else {
                stringBuilder.append(tableColumns.get(i)).append(" ");
            }
        }
        logger.info("insert into  user " + "( "+stringBuilder+" ) values ( 33,'dd',33,2021-08-15 22:10:53.383)");
    }

    //@Test
    public void testSave() {
        Repository<User, String> repository = new BaseRepository<>(jdbcOperation,User.class);
        User user = new User();
        user.setAge(2);
        user.setCreateTime(new Date());
        user.setId(33);
        user.setName("shabi");
        int save = repository.save(user);
        logger.info("插入的条数："+save);
        logger.info("插入的实体为："+repository.findAll());
    }

    @Test
    public void testUpate() {

        testSave();

        Repository<User, String> repository = new BaseRepository<>(jdbcOperation, User.class);
        User user = new User();
        user.setName("shabi_busha");
        user.setAge(32);
        user.setCreateTime(new Date());
        user.setId(66);
        boolean update = repository.update(user);
        logger.info("更新的状态："+update);

        logger.info("更改后的实体为："+repository.findAll());
    }
}
