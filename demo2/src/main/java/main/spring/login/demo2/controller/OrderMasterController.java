package main.spring.login.demo2.controller;

import main.spring.login.demo2.dto.*;
import main.spring.login.demo2.entity.Contact;
import main.spring.login.demo2.entity.OrderMaster;
import main.spring.login.demo2.entity.OrderProduct;
import main.spring.login.demo2.repository.OrderMasterRepository;
import main.spring.login.demo2.service.OrderMasterService;
import main.spring.login.demo2.service.OrderProductService;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderMasterController {

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired // 이 부분이 누락되어 있습니다.
    private OrderProductService orderProductService; // 이제 OrderProductService를 주입받아 사용할 수 있습니다.

    @GetMapping("/customer")
    public List<OrderMaster> getOrders() {
        return orderMasterService.findAllOrderMaster();
    }

    @GetMapping("/customer/{customerCode}")
    public List<OrderMaster> getOrdersByCustomerCode(@PathVariable("customerCode") String customerCode) {
        return orderMasterService.getOrderMastersByCustomerCode(customerCode);
    }

    //business id 에 따른 주문 목록
    @GetMapping("/id/{businessId}")
    public List<OrderMaster> getOrdersByBusinessId(@PathVariable("businessId") String businessId) {
        return orderMasterService.findByBusinessId(businessId);
    }

    @GetMapping("/customer/{customerCode}/products")
    public ResponseEntity<List<OrderProduct>> getOrderProductsByCustomerCode(@PathVariable("customerCode") String customerCode) {
        List<OrderMaster> orderMasters = orderMasterService.getOrderMastersByCustomerCode(customerCode);
        List<OrderProduct> orderProducts = new ArrayList<>();

        for(OrderMaster orderMaster : orderMasters) {
            orderProducts.addAll(orderProductService.getOrderProductsByOrderNumber(orderMaster.getOrderNumber()));
        }

        return ResponseEntity.ok(orderProducts);
    }

    @GetMapping("/adjustment")
    public ResponseEntity<List<OrderMaster>> getAllOrderMasters() {
        List<OrderMaster> orderMasters = orderMasterService.findAllOrderMaster();
        return ResponseEntity.ok(orderMasters);
    }


    // 특정 businessId의 orderNumber에 해당하는 주문을 가져오는 엔드포인트
    @GetMapping("/id/{businessId}/{orderNumber}")
    public ResponseEntity<OrderMaster> getOrderByIdAndOrderNumber(
            @PathVariable("businessId") String businessId,
            @PathVariable("orderNumber") Integer orderNumber) {
        return ResponseEntity.ok(orderMasterService.findByBusinessIdAndOrderNumber(businessId, orderNumber));
    }

    // 현재 달에 해당하는 특정 코드와 같은 데이터를 모두 검색하고, 그 중에서 '미정산' 상태와 '정산 완료' 상태의 주문 개수를 계산
    @GetMapping("/{customerContact}/count")
    public ResponseEntity<Map<String, Integer>> getOrderCountByCurrentMonthAndCustomerContact(@PathVariable String customerContact) {
        return ResponseEntity.ok(orderMasterService.countOrderStatusByCurrentMonth(customerContact));
    }

    @PutMapping("/adjustment/{orderNumber}") //정산 상태 바뀐 것 받아오는 controller
    public ResponseEntity<OrderMaster> updateOrderStatus(@PathVariable("orderNumber") Integer orderNumber, @RequestBody OrderMasterDTO orderMasterDTO) {
        OrderMaster updatedOrder = orderMasterService.updateOrderStatus(orderNumber, orderMasterDTO.getAdjustmentStatus());
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/post")
    public OrderMaster createOrder(@RequestBody OrderMaster orderMaster) {
        return orderMasterRepository.save(orderMaster);
    }

    @PutMapping("/{orderNumber}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable int orderNumber) {
        orderMasterService.cancelOrder(orderNumber);
        return ResponseEntity.ok().body("Order has been canceled successfully");
    }
     
    @GetMapping("/{orderNumber}")
    public Optional<OrderMaster> getOrdersByCustomerCode(@PathVariable("orderNumber") int orderNumber) {
        return orderMasterService.findByOrderNumber(orderNumber);
    }

    @GetMapping("/busId/{businessId}")
    public List<OrderMasterYDto> getOrderDtoByBusinessId(@PathVariable("businessId") String businessId) {
        return orderMasterService.findOrderMasterDtoByBusinessId(businessId);
    }

    @GetMapping("/sc/{storageCode}")
    public List<OrderMaster> getOrderByStorageCode(@PathVariable("storageCode") String storageCode) {
        return orderMasterService.findByStorageCode(storageCode);
    }

    @GetMapping("/pendingsettlement/{customerCode}")
    public ResponseEntity<Boolean> isPendingSettlement(@PathVariable("customerCode") String customerCode) {
        boolean pendingSettlement = orderMasterService.isPendingSettlement(customerCode);
        return ResponseEntity.ok(pendingSettlement);
    }

    @GetMapping("/req/{businessId}")
    public List<ContactYDto> findStatusByBusinessId(@PathVariable("businessId") String businessId) {
        return orderMasterService.findStatusByBusinessId(businessId);
    }

    @GetMapping("/chart1/{businessId}")
    public List<Chart1Dto> getTotalOrderPriceByBusId(@PathVariable("businessId") String businessId) {
        return orderMasterService.getTotalOrderPriceByBusId(businessId);
    }

    @GetMapping("/chart2/{businessId}")
    public List<ContactYDto> findCusByBusinessId(@PathVariable("businessId") String businessId) {
        return orderMasterService.findCusByBusinessId(businessId);
    }

    @GetMapping("/chart3/{customerCode}")
    public List<Chart2Dto> findOrderCntByCustomerCode(@PathVariable("customerCode") String customerCode) {
        return orderMasterService.findOrderCntByCustomerCode(customerCode);
    }

    @GetMapping("/request/{businessId}")
    public List<Contact1YDto> findReqStatusByBusinessId(@PathVariable("businessId") String businessId){
        return orderMasterService.findReqStatusByBusinessId(businessId);
    }

}
