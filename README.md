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


#### 21.03.10 스프링 빈 조회하기

```JAVA
    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        //ac.getBeanDefinitionNames()로 스프링에 등록된 모든 빈 이름을 조회.
        //ac.getBean()으로 빈 객체(인스턴스)를 조회.
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            //우리에게 필요한, 우리가 Bean으로 지정해 준 것들만 보고 싶을때 해당 조건문으로 가져올 수 있다.
            //Role ROLE_APPLICATION : 직접 등록한 애플리케이션 빈
            //Role ROLE_INFRASTRUCTURE : 스프링이 내부에서 사용하는 빈
            if(beanDefinition.getRole() == BeanDefinition.ROLE_INFRASTRUCTURE) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = "+ beanDefinitionName + " object = " + bean);
            }
        }
    }
```
1. `ac.getBeanDefinitionNames()`로 모든 Bean의 이름을 가져올 수 있다.
2. `BeanDefinition.ROLE_APPLICATION`은 우리가 직접 생성한 빈만 가져와 보여준다.
3. `beanDefinition.getRole()`을 통해 해당 빈이 개발자가 직접 생성한 것인지, 아니면 스프링 내부에 원래부터 존재하는 것인지를 비교해서 조건문을 통해 정확한 빈 정보를 보여줄 수 있다.

** Tip **

*배열을 입력하였을 경우*

ex) `String[] beanDefinitionNames = ac.getBeanDefinitionNames();`

배열 바로 아래에 `iter + TAB`을 누르면 for문이 자동 생성된다.
```java
for (String beanDefinitionName : beanDefinitionNames) {
    
}
```
