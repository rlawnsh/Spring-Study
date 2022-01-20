package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 가급적이면 연관관계 필드는 @ToString 에서 제외시켜줘야 한다.
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
// @NamedQuery - 애플리케이션 로딩 시점에 오류를 잡을 수 있음
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    // 양방향 연관관계 주의
    // 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정하자
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
