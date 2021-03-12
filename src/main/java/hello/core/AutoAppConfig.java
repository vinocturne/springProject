package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration //설정 정보니까 @Configuration을 붙여줌
@ComponentScan ( //전체를 긁어서 스프링 빈을 자동 생성해주는 코드 @Component 어노테이션이 붙은 클래스를 찾아 자동으로 컨테이너에 등록.
        basePackages = "hello.core.member", //기본 시작 패키지 지정 후 하위 폴더로 넘어가면서 등록한다.
        //패키지를 등록하면 필요한 부분만 찾을 수 있어서 빠르게 찾을 수 있다.
        //basePackages = {"hello.core", "hello.service"}와 같이 시작 위치를 여러개 지정 가능
        //지정하지 않을 경우, 해당 설정 정보 클래스가 위치한 클래스의 패키지가 시작 위치가 된다.

        //따라서 관례상 설정 정보 클래스는 프로젝트 최상단으로 두는 것이 좋다.
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
        //Configuration 클래스를 제외해주는 이유가, 수동으로 빈을 설정해준 AppConfig.class에 @Configuration이 붙어있는데,
        //@Configuration에는 @Component가 포함되어있기 때문에 충돌이 날 수 있다.
)
public class AutoAppConfig {

}
