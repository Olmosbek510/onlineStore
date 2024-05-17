package uz.inha.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import uz.inha.entity.Attachment;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AttachmentRepo {
    private final JdbcTemplate jdbcTemplate;
    private final AttachmentContentRepo attachmentContentRepo;

    public UUID sava(Attachment attachment) {
        String query = "insert into attachment(type) values(?)";
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement preparedStatement = con.prepareStatement(query, new String[]{"id"});
            preparedStatement.setString(1, attachment.getType());
            return preparedStatement;
        };
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatementCreator, holder);
        Map<String, Object> keys = holder.getKeys();
        assert keys != null;
        return (UUID) keys.get("id");
    }

    public void remove(UUID attachmentId) {
        attachmentContentRepo.removeByAttachment(attachmentId);
        String sql = "delete from attachment where id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", attachmentId);
        jdbcTemplate.update(sql,source);
    }
}
