package uz.inha.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.inha.entity.OrderProduct;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderProductRepo {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public void save(OrderProduct orderProduct){
        String sql = "insert into order_product(product_id,amount,order_id) values(:product_id, :amount, :order_id)";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("product_id", orderProduct.getProductId());
        source.addValue("amount", orderProduct.getAmount());
        source.addValue("order_id", orderProduct.getOrderId());
        namedParameterJdbcTemplate.update(sql, source);
    }

    public List<OrderProduct> findByOrder(UUID id) {
        String sql = "select * from order_product where order_id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", id);
        List<OrderProduct> query = namedParameterJdbcTemplate.query(sql, source, BeanPropertyRowMapper.newInstance(OrderProduct.class));
        return query;
    }
}
