package uz.inha.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.inha.entity.Category;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CategoryRepo {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void add(Category category) {
        String sql = "insert into category(name) values(:name)";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("name", category.getName());
        namedParameterJdbcTemplate.update(sql, source);
    }

    public List<Category> findAll() {
        var sql = "select * from category order by name";
        return namedParameterJdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Category.class));
    }

    public Category findById(UUID id) {
        String sql = "select * from category where id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, source, BeanPropertyRowMapper.newInstance(Category.class));
    }

    public void edit(UUID id, Category category) {
        String sql = "update category set name = :name where id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("name", category.getName());
        source.addValue("id", id);
        namedParameterJdbcTemplate.update(sql, source);
    }

    public void deleteById(UUID uuid) {
        try {
            String sql = "delete from category where id = :id";
            namedParameterJdbcTemplate.update(sql, Map.of("id", uuid));
        } catch (Exception ignored) {
        }

    }
}
