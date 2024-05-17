package uz.inha.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.inha.dto.ProductDto;
import uz.inha.entity.AttachmentContent;
import uz.inha.entity.Product;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepo {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final AttachmentContentRepo attachmentContentRepo;

    public void save(Product product) {
        String sql = "insert into product(name, price, category_id, photo_id) values (:name, :price, :category_id, :photo_id)";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("name", product.getName());
        source.addValue("price", product.getPrice());
        source.addValue("category_id", product.getCategoryId());
        source.addValue("photo_id", product.getPhotoId());
        namedParameterJdbcTemplate.update(sql, source);
    }

    public List<ProductDto> findAll() {
        String sql = """
                select p.id as id, p.name as name, p.price as price, c.name as category, c.id as categoryId, p.photo_id as photoId, a.content as photo
                from product p
                join category c on p.category_id = c.id
                join attachment_content a on p.photo_id = a.attachment_id order by p.name
                """;
        RowMapper<ProductDto> productDtoRowMapper = (rs, rowNum) -> new ProductDto(
                (UUID) rs.getObject("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getString("category"),
                (UUID) rs.getObject("categoryId"),
                (UUID) rs.getObject("photoId"),
                rs.getBytes("photo")
        );
        return jdbcTemplate.query(sql, productDtoRowMapper);
    }

    public Product findById(UUID id) {
        String sql = "select * from product where id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, source, BeanPropertyRowMapper.newInstance(Product.class));
    }

    public void edit(UUID id, Product product) {
        String sql = "update product set name = :name, price = :price, category_id = :catId, photo_id = :p_id where id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("name", product.getName());
        source.addValue("price", product.getPrice());
        source.addValue("catId", product.getCategoryId());
        source.addValue("p_id", product.getPhotoId());
        source.addValue("id", id);
        namedParameterJdbcTemplate.update(sql, source);
    }

    public void deleteById(UUID id) {
        try {
            String sql = "delete from product where id = :id";
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", id);
            namedParameterJdbcTemplate.update(sql, source);
        } catch (Exception ignored) {
        }
    }

    public List<Product> findByCategory(UUID categoryId) {
        String sql = "select * from product where category_id = :catId order by name";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("catId", categoryId);
        List<Product> products = namedParameterJdbcTemplate.query(sql, source, BeanPropertyRowMapper.newInstance(Product.class));
        for (Product product : products) {
            AttachmentContent content = attachmentContentRepo.findByAttachmentId(product.getPhotoId());
            product.setBase64Photo(Base64.getEncoder().encodeToString(content.getContent()));
        }
        return products;
    }
    public List<Product> findAllActualProduct(){
        String sql = "select * from product order by name";
        List<Product> products = namedParameterJdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Product.class));
        for (Product product : products) {
            AttachmentContent content = attachmentContentRepo.findByAttachmentId(product.getPhotoId());
            product.setBase64Photo(Base64.getEncoder().encodeToString(content.getContent()));
        }
        return products;
    }
}
