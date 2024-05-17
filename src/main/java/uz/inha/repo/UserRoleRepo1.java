package uz.inha.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import uz.inha.entity.Role;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRoleRepo1 {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public List<Role> findByUser(UUID id) {
        String sql = """
                select roles.* from roles join public.user_role ur on roles.id = ur.role_id and ur.user_id = :user_id
                """;
        return namedParameterJdbcTemplate.query(sql, Map.of("user_id", id), BeanPropertyRowMapper.newInstance(uz.inha.entity.Role.class));
    }

    public void save(UUID userId, Integer roleId) {
        simpleJdbcInsert.withTableName("user_role")
                .usingColumns("user_id","role_id")
                .execute(Map.of(
                        "user_id",userId,
                        "role_id",roleId
                ));
    }
}
