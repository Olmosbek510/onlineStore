package uz.inha.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import uz.inha.dto.BasketProductDto;
import uz.inha.entity.Order;
import uz.inha.entity.OrderProduct;
import uz.inha.entity.Product;
import uz.inha.entity.User;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class OrderRepo {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final OrderProductRepo orderProductRepo;
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    public UUID make(User user) {
        String sql = "insert into orders(user_id) values(?)";
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setObject(1, user.getId());
            return preparedStatement;
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        Map<String, Object> keys = keyHolder.getKeys();
        return (UUID) Objects.requireNonNull(keys).get("id");
    }

    public List<Order> findAll() {
        String sql = "select * from orders order by date_time desc";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Order.class));
    }

    public List<Order> findByUser(UUID userId) {
        String sql = "select * from orders where user_id = :u_id";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("u_id", userId);
        return namedParameterJdbcTemplate.query(sql, source, BeanPropertyRowMapper.newInstance(Order.class));
    }
    public Double getTotalPrice(UUID orderId){
        List<OrderProduct> orderProducts = orderProductRepo.findByOrder(orderId);
        return orderProducts.stream().mapToDouble(value -> value.getAmount() * productRepo.findById(value.getProductId()).getPrice()).sum();
    }

    public Order findById(UUID id){
        String sql = "select * from orders where id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, source, BeanPropertyRowMapper.newInstance(Order.class));
    }
    public List<BasketProductDto> getProducts(UUID id) {
        List<OrderProduct> orderProducts = orderProductRepo.findByOrder(id);
        List<BasketProductDto> basketProductDtos = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            Product product = productRepo.findById(orderProduct.getProductId());
            basketProductDtos.add(
                    new BasketProductDto(
                            orderProduct.getProductId(),
                            orderProduct.getAmount(),
                            product.getName(),
                            categoryRepo.findById(product.getCategoryId()).getName(),
                            product.getPrice(),
                            productRepo.findAll().stream().filter(productDto -> orderProduct.getProductId().equals(productDto.id())).findFirst().get().getPhoto()
                    )
            );
        }
        return basketProductDtos;
    }


}
