# springProject Repository / 개인 공부용

#### 스프링 강의를 들으며 공부한 내용들을 따로 코드와 함께 정리해서 저장해놓기 위한 레포지토리입니다.
### 21.03.09
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


### 21.03.10 스프링 빈 조회하기

```java
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

### 21.03.11 싱글톤 패턴

#### 싱글톤 패턴의 장점
1. 웹 애플리케이션을 이용하는 유저들이 객체를 생성할 때마다 참조값이 다 다른 같은 객체를 생성하게 되는데, 싱글톤은 이를 줄여주어 과부하를 줄여준다.
2. 클래스의 인스턴스가 ***딱 1개만 생성***되는 것을 보장한다.

```java
public class SingletonService {
    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }
    //생성자를 private으로 설정해서 외부에서 new 키워드로 생성할 수 없도록 만든다.
    //무조건 꺼내 쓸 때에는 getInstance로만 꺼내 쓸 수 있도록 한다.
    private SingletonService() {

    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
```

싱글톤은 인스턴스가 하나만 생성되어야 하기 때문에 생성자를 **private으로 설정해 외부에서 new 키워드로 객체 인스턴스가 생성 되는것을 방지**하고, 미리 static을 이용해 객체를 생성해 놓는다.(위의 싱글톤패턴의 경우 여러 방법 중 하나.)
외부에서 해당 객체를 불러올 때에는 `getInstance()`로 참조할 수 있다.

싱글톤 패턴을 적용하면 고객의 요청이 올 때마다 객체를 생성하는 것이 아닌, 이미 만들어진 객체를 공유해서 효율적으로 사용할 수 있다.

#### 싱글톤 패턴의 문제점
- 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
- 의**존관계상 클라이언트가 구체 클래스에 의존.**
- 클라이언트가 구체 클래스에 의존해서 **OCP원칙을 위반할 가능성이 높다.**
- 테스트하기 어렵다.
- 내부 속성을 변경하거나 초기화하기 어렵다.
- private 생성자로 자식 클래스를 만들기 어렵다.
- **유연성이 떨어진다**
- **안티패턴**으로 불리운다.

#### 하지만 스프링 컨테이너를 활용하면 싱글톤 패턴의 문제점은 해결하면서, 객체 인스턴스를 싱글톤으로 관리하는 것이 가능.


### 21.03.12 @ComponentScan을 통한 자동 빈 생성 및 @Autowired를 통한 의존성 주입

AppConfig 클래스를 통해 수동으로 빈을 생성함과 동시에 의존성을 주입할 수 있다.

```java
@Configuration //어플리케이션의 설정 정보를 나타낸다는 어노테이션
public class AppConfig {

    @Bean //스프링 컨테이너에 해당 Bean이 등록된다.
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
        //해당 부분에서 memberRepository를 생성하며 의존성을 주입한다.
    }
    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }
    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
```
위의 설정 클래스에서 해당 생성자를 통해 의존성을 주입한다.
```java
public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

```

**하지만 자동으로 빈을 생성하기 위해 @ComponentScan을 사용할 경우,**
```java
@Configuration
@ComponentScan
public class AutoAppConfig {

}
```
위의 코드만으로 설정 창이 끝나기 때문에 따로 의존성 주입을 해줄 수 있는 방법이 없다.

`@ComponentScan`어노테이션은 모든 클래스를 검색하면서 `@Component`가 붙은 클래스를 찾아 빈에 등록해준다.
`@ComponentScan`은 스캔을 시작할 위치를 지정해줄 수 있는데, default가 현재 `@ComponentScan`을 사용하는 설정 클래스의 패키지이기 때문에 가능하면 default값으로 사용하고, 설정 파일은 프로젝트 최상단에 두는 것을 추천한다.

이로서 자동으로 빈을 생성해주는 것은 문제가 없는데, 설정 클래스에서 각 Bean마다 의존성을 주입하는 부분이 사라졌다. 이 부분은 `@Autowired`로 해결이 가능한데,
`@Component`를 사용한 빈의 생성자 부분에 `@Autowired`를 추가하면 자동으로 의존성 주입까지 완료된다.

```java
@Component
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Autowired //ac.getBean(MemberRepository.class)을 쓴 것 같이, 의존관계를 자동 주입해준다.
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
```

#### 수동등록 빈 vs 자동등록 빈
- 수동등록한 빈이 있다면 수동등록한 빈이 우선권을 가지게 되어 overriding 된다.
- 다만 요즘에는 의도적으로 이런 결과를 기대하는 것 보다는 우연치않게 꼬이는 경우가 많기 때문에, 스프링 자체에서 자동빈과 수동빈이 충돌할 경우, 오류 메세지를 출력한다.
```java
Consider renaming one of the beans or enabling overriding by setting 
spring.main.allow-bean-definition-overriding=true
```
`spring.main.allow-bean-definition-overiding` 부분의 기본값을 false로 되어있는데, 이 부분을 true로 바꿔주면 수동 등록 빈이 자동 등록 빈을 덮어쓸 수 있다.
