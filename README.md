# springProject Repository / 개인 공부용
### 스프링 강의를 들으며 공부한 내용들을 따로 코드와 함께 정리해서 저장해놓기 위한 레포지토리입니다.
--------------
#### 21.03.09
1. memberService, orderService를 스프링을 사용하지 않은 상태로 AppConfig를 이용해서 의존성 주입.
2. AppConfig.java에 어플리케이션의 설정 정보를 나타내는 @Configuration 어노테이션 생성 및 @Bean을 통해 빈 생성

```JAVA
@Configuration //어플리케이션의 설정 정보를 나타낸다는 어노테이션
public class AppConfig {

    @Bean //스프링 컨테이너에 해당 Bean이 등록된다.
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
```
