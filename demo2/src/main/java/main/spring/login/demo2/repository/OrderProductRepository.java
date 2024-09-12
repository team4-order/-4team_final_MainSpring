package main.spring.login.demo2.repository;

import main.spring.login.demo2.dto.OrderProductDto;
import main.spring.login.demo2.dto.OrderProductYDto;
import main.spring.login.demo2.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
  List<OrderProduct> findByOrderNumber(Integer orderNumber);

  @Query("SELECT new main.spring.login.demo2.dto.OrderProductDto(op.orderNumber, op.goodsCode, gm.goodsName, op.goodsGrade, op.orderQuantity, op.orderPrice) "
        + "FROM OrderProduct op LEFT JOIN GoodsMaster gm ON op.goodsCode = gm.goodsCode WHERE op.orderNumber = :orderNumber")
  List<OrderProductDto> findOrderProductByOrderNumber(@Param("orderNumber") Integer orderNumber);

  //OrderProductYDto(Integer orderNumber, String goodsCode, String goodsGrade, Integer orderQuantity, String orderStatus, String storageCode)
  @Query("SELECT new main.spring.login.demo2.dto.OrderProductYDto(op.orderNumber, op.goodsCode, op.goodsGrade, op.orderQuantity, om.orderStatus, om.storageCode)"
        + "FROM OrderMaster om RIGHT OUTER JOIN OrderProduct op ON om.orderNumber = op.orderNumber WHERE om.storageCode = :storageCode AND (om.orderStatus = '출고 준비 중' OR om.orderStatus = '주문 완료')")
  List<OrderProductYDto> findOrderProductByStorageCode(@Param("storageCode") String storageCode);
}
