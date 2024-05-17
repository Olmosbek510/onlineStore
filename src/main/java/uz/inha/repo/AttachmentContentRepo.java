package uz.inha.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.inha.entity.AttachmentContent;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AttachmentContentRepo {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public void save(AttachmentContent attachmentContent) {
        String sql = "insert into attachment_content(attachment_id,content) values(:attachment_id, :content)";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("attachment_id", attachmentContent.getAttachmentId());
        source.addValue("content", attachmentContent.getContent());
        namedParameterJdbcTemplate.update(sql,source);
    }

    public AttachmentContent findByAttachmentId(UUID id) {
        String sql = "select * from attachment_content where attachment_id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, source, BeanPropertyRowMapper.newInstance(AttachmentContent.class));
    }

    public void removeByAttachment(UUID attachmentId) {
        String sql = "delete from attachment_content where attachment_id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("attachment_id", attachmentId);
        namedParameterJdbcTemplate.update(sql,source);
    }
}
