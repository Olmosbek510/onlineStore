package uz.inha.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.inha.entity.Role;

@Repository
@RequiredArgsConstructor
public class RoleRepo {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public Role findByCode(String code) {
        String sql = "select * from roles where code = :code";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("code",code);
        return namedParameterJdbcTemplate.queryForObject(sql,source, BeanPropertyRowMapper.newInstance(Role.class));
    }
}
