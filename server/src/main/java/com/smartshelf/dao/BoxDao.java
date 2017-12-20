package com.smartshelf.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.smartshelf.model.Box;

@Repository
@Transactional
public class BoxDao extends AbstractGenericDao<Box> {

	@PersistenceContext
	private EntityManager em; 
	
   public void add(Box box) {
      em.persist(box);
   }

   public List<Box> listPersons() {
      CriteriaQuery<Box> criteriaQuery = em.getCriteriaBuilder().createQuery(Box.class);
      @SuppressWarnings("unused")
      Root<Box> root = criteriaQuery.from(Box.class);
      return em.createQuery(criteriaQuery).getResultList();
   }
}
