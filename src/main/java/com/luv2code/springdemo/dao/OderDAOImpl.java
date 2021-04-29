package com.luv2code.springdemo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.DataOrderInterval;
import com.luv2code.springdemo.entity.Order;


@Repository
public class OderDAOImpl implements OrderDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	public void saveOrder(Order theOrder,HashMap<Integer, Integer> productsMap) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		theOrder.setStatus(1);
		currentSession.save(theOrder);
		int orderId= theOrder.getId();
		Query query;
		 
	    for (Map.Entry<Integer, Integer> entry : productsMap.entrySet()) {
	    	
	    query = currentSession.createQuery("update OrderDetail set quantity = :quantity where orderId = :orderId AND"
	    		+ " productId = :productId");
	    query.setParameter("quantity", entry.getValue());
        query.setParameter("orderId", orderId);
        query.setParameter("productId", entry.getKey());
        
        query.executeUpdate();     
	    }

	}

	@Override
	public List<Order> getOrders() {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Order> theQuery = 
				currentSession.createQuery("from Order order by id",
						Order.class);
		
		
		List<Order> orders = theQuery.getResultList();
		
		return orders;
	}

	@Override
	public void deleteOrder(int id) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query theQuery = 
				currentSession.createQuery("delete from Order where id=:orderId");
		theQuery.setParameter("orderId", id);
		
		theQuery.executeUpdate();
		
	}

	@Override
	public Order getOrderById(int id) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Order tempOrder = currentSession.get(Order.class, id);
		return tempOrder;
	}

	@Override
	public void updateOrder(Order theOrder) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query query = currentSession.createQuery("update Order set userName = :nameParam, userPhone = :PhoneParam" +
                ",userComment = :CommentParam , status=:StatusParam, dateTime=:DataParam"+
                " where id = :idParam");

        query.setParameter("nameParam", theOrder.getUserName());
        query.setParameter("PhoneParam", theOrder.getUserPhone());
        query.setParameter("CommentParam",theOrder.getUserComment() );
        query.setParameter("StatusParam", theOrder.getStatus());
        query.setParameter("DataParam", theOrder.getDateTime());
        query.setParameter("idParam", theOrder.getId());

        query.executeUpdate();
		
	}

	@Override
	public Double totalPrice(int orderId) {
			
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Double> theQuery = currentSession.createQuery("SELECT SUM(e.price*b.quantity) FROM Product e,OrderDetail b WHERE e.id=b.productId AND b.orderId=:Id",Double.class);
		theQuery.setParameter("Id", orderId);
		
		return theQuery.getResultList().get(0);
	}

	@Override
	public HashMap<Integer, Integer> getQuantity(int theId) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		Query theQuery = currentSession.createQuery(" select new map(orderDetail.productId as productId, orderDetail.quantity as quantity)"
				+ " from OrderDetail orderDetail WHERE orderDetail.orderId=:id");
		theQuery.setParameter("id", theId);
		List<Map<Integer,Integer>> list = theQuery.list();
		
		Map<Integer, Integer> result  = new HashMap<Integer, Integer>();
		
		for(Map<Integer, Integer> a:list) {
			result.put(a.get("productId"),a.get("quantity"));
		}

		return (HashMap<Integer, Integer>) result;
	}

	@Override
	public List<Order> getOrdersFromInterval(DataOrderInterval dataInterval) {
		
			Session currentSession = sessionFactory.getCurrentSession();	
			
		DataOrderInterval currentEditInterval = new DataOrderInterval(dataInterval);
		currentEditInterval.editInterval();
		
		Query<Order> theQuery = 
	    currentSession.createQuery("from Order e where e.dateTime  BETWEEN :start AND :end order by id",Order.class);
			
		theQuery.setParameter("start", currentEditInterval.getStartData());
		theQuery.setParameter("end", currentEditInterval.getEndData());
		
		List<Order> orders = theQuery.getResultList();
		
		return orders;
		
		
	}
	
}
