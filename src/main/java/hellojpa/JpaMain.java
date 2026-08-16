package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.logging.Logger;

public final class JpaMain {
    private static final Logger LOGGER = Logger.getLogger(JpaMain.class.getName());

    private JpaMain() {
    }

    public static void main(String[] args) {
        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
             EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            persistAndReadMember(entityManager);
        }
    }

    private static void persistAndReadMember(EntityManager entityManager) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            Team team = new Team();
            team.setName("TeamA");
            Member member = new Member();
            member.setUsername("member1");
            team.addMember(member);
            entityManager.persist(team);
            entityManager.persist(member);
            entityManager.flush();
            entityManager.clear();
            Member foundMember = entityManager.find(Member.class, member.getId());
            LOGGER.info(() -> "findTeam = " + foundMember.getTeam().getName());
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }
}
