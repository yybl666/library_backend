package ybl.lib.backend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import ybl.lib.backend.pojo.User;

/**
 * 用户表的数据访问接口。
 */
@Mapper
public interface UserMapper {

    /**
     * 按用户名查询用户，供登录使用。
     *
     * <p>返回结果包含 password 字段（MD5 哈希），用于上层比对密码。
     * 该字段只应在服务层内部流转，不可直接返回给前端。
     *
     * @param name 用户名
     * @return 对应用户，不存在时返回 null
     */
    @Select("SELECT id, name, password, email, role FROM `user` WHERE name = #{name}")
    User findByName(@Param("name") String name);

    /**
     * 按 id 查询用户，供查询当前用户信息使用。
     *
     * @param id 用户 id
     * @return 对应用户，不存在时返回 null
     */
    @Select("SELECT id, name, password, email, role FROM `user` WHERE id = #{id}")
    User findById(@Param("id") Long id);

    /**
     * 统计该用户名已存在的记录数，供注册时查重。
     *
     * @param name 用户名
     * @return 记录数，0 表示可用
     */
    @Select("SELECT COUNT(*) FROM `user` WHERE name = #{name}")
    int countByName(@Param("name") String name);

    /**
     * 统计该邮箱已存在的记录数，供注册时查重。
     *
     * @param email 邮箱
     * @return 记录数，0 表示可用
     */
    @Select("SELECT COUNT(*) FROM `user` WHERE email = #{email}")
    int countByEmail(@Param("email") String email);

    /**
     * 新增用户。
     *
     * <p>id 由数据库自增生成，插入后通过 useGeneratedKeys 回填到入参对象的 id 字段。
     *
     * @param user 待插入的用户，password 必须是 MD5 后的哈希值
     * @return 影响行数，成功为 1
     */
    @Insert("INSERT INTO `user` (name, password, email, role) "
            + "VALUES (#{name}, #{password}, #{email}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    /**
     * 修改指定用户的密码。
     *
     * @param id       用户 id
     * @param password 新的密码哈希值，调用方需先做 MD5
     * @return 影响行数，成功为 1
     */
    @Update("UPDATE `user` SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);
}
