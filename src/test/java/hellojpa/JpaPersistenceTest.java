package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JpaPersistenceTest {
    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("hello");
    }

    @AfterEach
    void tearDown() {
        entityManagerFactory.close();
    }

    @Test
    void persistsAndLoadsMemberWithTeam() {
        Long memberId;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            Team team = new Team();
            team.setName("TeamA");
            Member member = new Member();
            member.setUsername("member1");
            team.addMember(member);
            entityManager.persist(team);
            entityManager.persist(member);
            transaction.commit();
            memberId = member.getId();
        }
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Member foundMember = entityManager.find(Member.class, memberId);
            assertEquals("member1", foundMember.getUsername());
            assertEquals("TeamA", foundMember.getTeam().getName());
        }
    }

    @Test
    void queriesPersistedMembers() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            Member first = new Member();
            first.setUsername("alpha");
            Member second = new Member();
            second.setUsername("beta");
            entityManager.persist(first);
            entityManager.persist(second);
            transaction.commit();
        }
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            List<Member> members = entityManager.createQuery("select m from Member m order by m.username", Member.class).getResultList();
            assertEquals(List.of("alpha", "beta"), members.stream().map(Member::getUsername).toList());
        }
    }

    @Test
    void maintainsTeamRelationshipWithoutExposingMutableCollection() {
        Team team = new Team();
        Member member = new Member();
        team.addMember(member);
        assertSame(team, member.getTeam());
        assertEquals(List.of(member), team.getMembers());
        assertThrows(UnsupportedOperationException.class, () -> team.getMembers().clear());
    }
}
