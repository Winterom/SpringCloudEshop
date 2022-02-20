package core.repositories;

import core.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    default List<Product> getMostBuyingProduct(EntityManager em){
        Query query = em.createQuery("select oi.product  from OrderItem as oi group by oi.product ORDER BY count(oi.quantity)  DESC ");
        query.setMaxResults(5);
        return (List<Product>) query.getResultList();
    }
}
