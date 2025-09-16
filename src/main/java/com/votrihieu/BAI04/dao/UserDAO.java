package com.votrihieu.BAI04.dao;

import java.util.List;

import com.votrihieu.BAI04.model.User;
import com.votrihieu.BAI04.utils.JpaUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class UserDAO {

	public User checkLogin(String username, String password) {
		EntityManager em = JpaUtil.getEntityManager();
		try {
			// Sử dụng JPQL (Java Persistence Query Language) để truy vấn
			String jpql = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password";
			TypedQuery<User> query = em.createQuery(jpql, User.class);
			query.setParameter("username", username);
			query.setParameter("password", password);

			// getSingleResult() sẽ ném ra NoResultException nếu không tìm thấy kết quả
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	public User findById(int id) {
		EntityManager em = JpaUtil.getEntityManager();
		try {
			return em.find(User.class, id);
		} finally {
			em.close();
		}
	}

	public void update(User user) {
		EntityManager em = JpaUtil.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.merge(user); // Dùng merge để cập nhật một đối tượng đã tồn tại
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	public User findByUsername(String username) {
		EntityManager em = JpaUtil.getEntityManager();
		try {
			TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
			query.setParameter("username", username);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null; // Không tìm thấy
		} finally {
			em.close();
		}
	}

	public User findByEmail(String email) {
		EntityManager em = JpaUtil.getEntityManager();
		try {
			TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
			query.setParameter("email", email);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null; // Không tìm thấy
		} finally {
			em.close();
		}
	}

	public void insert(User user) {
		EntityManager em = JpaUtil.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.persist(user);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	public List<User> findAll() {
		EntityManager em = JpaUtil.getEntityManager();
		try {
			TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	public void delete(int id) {
		EntityManager em = JpaUtil.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			User user = em.find(User.class, id);
			if (user != null) {
				em.remove(user);
			}
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	public List<User> searchByUsernameOrFullname(String keyword) {
		EntityManager em = JpaUtil.getEntityManager();
		try {
			String jpql = "SELECT u FROM User u WHERE u.username LIKE :keyword OR u.fullname LIKE :keyword";
			TypedQuery<User> query = em.createQuery(jpql, User.class);
			query.setParameter("keyword", "%" + keyword + "%");
			return query.getResultList();
		} finally {
			em.close();
		}
	}

}