package uz.inha.repo;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import uz.inha.entity.User;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepo {
    private final JdbcTemplate jdbcTemplate;
    @SneakyThrows
    public User findByEmail(String username) {
        var sql = "select * from users where username = ?";
        RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
                (UUID) rs.getObject("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                new ArrayList<>()
        );
        User query = jdbcTemplate.queryForObject(sql, userRowMapper, username);
        System.out.println(query);
        return query;
    }

    public UUID save(User user) {
        String sql = "insert into users(username, password, first_name, last_name) values(?,?,?,?)";
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstname());
            preparedStatement.setString(4, user.getLastname());
            return preparedStatement;
        };
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatementCreator, generatedKeyHolder);
        Map<String, Object> keys = generatedKeyHolder.getKeys();
        assert keys != null;
        return keys.get("id") != null ? (UUID) keys.get("id") : null;
    }
}
