package com.votrihieu.BAI04.dao;

import java.util.List;
import com.votrihieu.BAI04.model.Video;
import com.votrihieu.BAI04.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class VideoDAO {

	public List<Video> findAll() {
		EntityManager em = JpaUtil.getEntityManager();
		try {
			return em.createQuery("SELECT v FROM Video v", Video.class).getResultList();
		} finally {
			em.close();
		}
	}

	public Video findById(int id) {
		EntityManager em = JpaUtil.getEntityManager();
		try {
			return em.find(Video.class, id);
		} finally {
			em.close();
		}
	}

	public void insert(Video video) {
		EntityManager em = JpaUtil.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.persist(video);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
		} finally {
			em.close();
		}
	}

	public void update(Video video) {
		EntityManager em = JpaUtil.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.merge(video);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
		} finally {
			em.close();
		}
	}

	public void delete(int id) {
		EntityManager em = JpaUtil.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			Video video = em.find(Video.class, id);
			if (video != null) {
				em.remove(video);
			}
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
		} finally {
			em.close();
		}
	}

	public List<Video> searchByTitle(String keyword) {
		EntityManager em = JpaUtil.getEntityManager();
		try {
			String jpql = "SELECT v FROM Video v WHERE v.title LIKE :keyword";
			TypedQuery<Video> query = em.createQuery(jpql, Video.class);
			query.setParameter("keyword", "%" + keyword + "%");
			return query.getResultList();
		} finally {
			em.close();
		}
	}
}