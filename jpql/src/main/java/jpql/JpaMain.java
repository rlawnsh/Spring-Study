package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory enf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = enf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.changeTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.changeTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.changeTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            // 프로젝션(select)
//            List<MemberDTO> resultList = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
//                    .getResultList();

            // 페이징
//            List<Member> resultList = em.createQuery("select m from Member m order by m.age desc", Member.class)
//                    .setFirstResult(1)
//                    .setMaxResults(10)
//                    .getResultList();

            // 조인
//            List<Member> resultList = em.createQuery("select m from Member m left join m.team t on t.name = 'teamA'", Member.class)
//                    .getResultList();

            // 조건식(case)
//            String query =
//                    "select " +
//                            "case when m.age <= 10 then '학생요금' " +
//                            "     when m.age >= 60 then '경로요금' " +
//                            "     else '일반요금' " +
//                            "end " +
//                            "from Member m";

            // 다대일 페치 조인 (즉시 로딩) 페치 조인 대상에는 별칭을 줄 수 없다.
//            String query = "select m from Member m join fetch m.team";
//            List<Member> resultList = em.createQuery(query, Member.class)
//                    .getResultList();

            // 일대다 페치 조인 (데이터 뻥튀기 -> distinct 추가)
//            String query = "select distinct t from Team t join fetch t.members";
//            List<Team> resultList = em.createQuery(query, Team.class)
//                    .getResultList();

            // Named 쿼리
//            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
//                    .setParameter("username", "member1")
//                    .getResultList();

            // 벌크 연산 (영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리 날림, ex) update )
            // Flush 자동 호출 - commit, query, 강제 flush
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            // 영속성 컨텍스트 초기화
            em.clear();

            // em.clear()를 안하고 em.find()를 하면 벌크 연산으로 인해 영속성 컨텍스트에는 아직
            // age가 0으로 되있기 때문에 0이 출력된다 따라서 em.clear()로 영속성 컨텍스트를
            // 초기화 한 후 em.find()를 해야 영속성 컨텍스트에 아무 값이 없어 데이터베이스로 접근해
            // 값을 가져와 20이 출력된다.
            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember = " + findMember.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        enf.close();
    }
}
